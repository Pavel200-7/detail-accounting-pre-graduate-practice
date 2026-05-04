package com.example.demo.application.impl;

import com.example.demo.application.serv.RequestService;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.enums.MovementType;
import com.example.demo.domain.enums.RequestStatus;
import com.example.demo.infrastructure.exceptions.AccessDeniedException;
import com.example.demo.infrastructure.exceptions.BusinessRuleException;
import com.example.demo.infrastructure.exceptions.EntityNotFoundException;
import com.example.demo.infrastructure.mappers.RequestMapper;
import com.example.demo.infrastructure.repositories.*;
import com.example.demo.infrastructure.security.SecurityService;
import com.example.demo.infrastructure.security.enums.Roles;
import com.example.demo.presentation.dto.request.CreateRequestDto;
import com.example.demo.presentation.dto.request.RejectRequestDto;
import com.example.demo.presentation.dto.response.RequestWithItemsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestItemRepository requestItemRepository;
    private final PartRepository partRepository;
    private final StockRepository stockRepository;
    private final MovementRepository movementRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final SecurityService securityService;

    @Override
    @Transactional
    public RequestWithItemsResponseDto createRequest(CreateRequestDto dto) {
        // Получаем текущего пользователя
        UUID currentUserId = securityService.getCurrentUserIdAsUUID();
        User employee = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User", currentUserId));

        // Проверяем, что у пользователя есть отдел
        if (employee.getDepartment() == null) {
            throw new BusinessRuleException("Пользователь не прикреплен к отделу");
        }

        // Проверяем, что все Part существуют
        for (CreateRequestDto.RequestItemDto itemDto : dto.getItems()) {
            if (!partRepository.existsById(itemDto.getPartId())) {
                throw new EntityNotFoundException("Part", itemDto.getPartId());
            }
        }

        // Создаем Request
        Request request = requestMapper.toEntity(dto, employee, employee.getDepartment());
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setSubmittedAt(LocalDateTime.now());

        Request savedRequest = requestRepository.save(request);

        // Создаем RequestItems
        for (CreateRequestDto.RequestItemDto itemDto : dto.getItems()) {
            Part part = partRepository.findById(itemDto.getPartId()).get();

            RequestItem item = new RequestItem();
            item.setRequest(savedRequest);
            item.setPart(part);
            item.setQuantity(itemDto.getQuantity());

            requestItemRepository.save(item);
            savedRequest.getItems().add(item);
        }

        return requestMapper.toResponseDto(savedRequest);
    }

    @Override
    @Transactional
    public RequestWithItemsResponseDto approveRequest(UUID id) {
        // Проверяем роль MANAGER
        if (!securityService.hasRole(Roles.MANAGER.name)) {
            throw new AccessDeniedException("Требуется роль MANAGER");
        }

        Request request = requestRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("Request", id));

        // Проверяем статус
        if (request.getStatus() != RequestStatus.PENDING &&
                request.getStatus() != RequestStatus.NEED_PURCHASE) {
            throw new BusinessRuleException(
                    "Утвердить можно только заявку в статусе PENDING или NEED_PURCHASE. Текущий статус: " + request.getStatus()
            );
        }

        // Проверяем наличие достаточного количества на складе
        boolean hasEnough = true;
        for (RequestItem item : request.getItems()) {
            Stock stock = stockRepository.findByPartId(item.getPart().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock for Part", item.getPart().getId()));

            int available = stock.getQuantity() - stock.getReservedQuantity();
            if (available < item.getQuantity()) {
                hasEnough = false;
                break;
            }
        }

        if (hasEnough) {
            // Резервируем товары
            for (RequestItem item : request.getItems()) {
                Stock stock = stockRepository.findByPartId(item.getPart().getId()).get();
                stock.setReservedQuantity(stock.getReservedQuantity() + item.getQuantity());
                stock.setLastUpdated(LocalDateTime.now());
                stockRepository.save(stock);
            }

            request.setStatus(RequestStatus.APPROVED);
            request.setApprovedAt(LocalDateTime.now());
        } else {
            request.setStatus(RequestStatus.NEED_PURCHASE);
            requestRepository.save(request);
            throw new BusinessRuleException(
                    "Недостаточно товаров на складе. Заявка переведена в статус NEED_PURCHASE"
            );
        }

        Request updatedRequest = requestRepository.save(request);
        return requestMapper.toResponseDto(updatedRequest);
    }

    @Override
    @Transactional
    public RequestWithItemsResponseDto rejectRequest(RejectRequestDto dto) {
        // Проверяем роль MANAGER
        if (!securityService.hasRole(Roles.MANAGER.name)) {
            throw new AccessDeniedException("Требуется роль MANAGER");
        }

        Request request = requestRepository.findByIdWithItems(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Request", dto.getId()));

        // Проверяем статус
        if (request.getStatus() != RequestStatus.PENDING &&
                request.getStatus() != RequestStatus.NEED_PURCHASE &&
                request.getStatus() != RequestStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Отклонить можно только заявку в статусе PENDING, NEED_PURCHASE или APPROVED"
            );
        }

        // Если заявка была утверждена - откатываем резервирование
        if (request.getStatus() == RequestStatus.APPROVED) {
            for (RequestItem item : request.getItems()) {
                Stock stock = stockRepository.findByPartId(item.getPart().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Stock for Part", item.getPart().getId()));

                // Откатываем резервирование
                stock.setReservedQuantity(stock.getReservedQuantity() - item.getQuantity());
                stock.setLastUpdated(LocalDateTime.now());
                stockRepository.save(stock);

                // Создаем контр-движение (отмена резерва)
                Movement movement = new Movement();
                movement.setType(MovementType.RECEIPT);
                movement.setQuantity(item.getQuantity());
                movement.setQuantityBefore(stock.getQuantity());
                movement.setQuantityAfter(stock.getQuantity());
                movement.setDocumentType("REJECT_RESERVATION");
                movement.setPerformedAt(LocalDateTime.now());
                movement.setComment("Отмена резервирования по заявке " + request.getId());
                movement.setPart(item.getPart());

                UUID currentUserId = securityService.getCurrentUserIdAsUUID();
                User currentUser = userRepository.findById(currentUserId).get();
                movement.setPerformedBy(currentUser);

                movementRepository.save(movement);
            }
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(dto.getRejectionReason());

        Request updatedRequest = requestRepository.save(request);
        return requestMapper.toResponseDto(updatedRequest);
    }

    @Override
    @Transactional
    public RequestWithItemsResponseDto completeRequest(UUID id) {
        // Проверяем роль MANAGER или STOREKEEPER
        boolean isManager = securityService.hasRole(Roles.MANAGER.name);
        boolean isStorekeeper = securityService.hasRole(Roles.STOREKEEPER.name);

        if (!isManager && !isStorekeeper) {
            throw new AccessDeniedException("Требуется роль MANAGER или STOREKEEPER");
        }

        Request request = requestRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("Request", id));

        // Проверяем статус
        if (request.getStatus() != RequestStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Завершить можно только утвержденную заявку. Текущий статус: " + request.getStatus()
            );
        }

        // Убираем резервирование и создаем движения расхода
        for (RequestItem item : request.getItems()) {
            Stock stock = stockRepository.findByPartId(item.getPart().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock for Part", item.getPart().getId()));

            // Убираем из резерва
            stock.setReservedQuantity(stock.getReservedQuantity() - item.getQuantity());

            // Списываем со склада
            int quantityBefore = stock.getQuantity();
            stock.setQuantity(quantityBefore - item.getQuantity());
            stock.setLastUpdated(LocalDateTime.now());
            stockRepository.save(stock);

            // Создаем движение расхода
            Movement movement = new Movement();
            movement.setType(MovementType.CONSUMPTION);
            movement.setQuantity(item.getQuantity());
            movement.setQuantityBefore(quantityBefore);
            movement.setQuantityAfter(stock.getQuantity());
            movement.setDocumentType("REQUEST_COMPLETION");
            movement.setPerformedAt(LocalDateTime.now());
            movement.setComment("Выполнение заявки " + request.getId());
            movement.setPart(item.getPart());

            UUID currentUserId = securityService.getCurrentUserIdAsUUID();
            User currentUser = userRepository.findById(currentUserId).get();
            movement.setPerformedBy(currentUser);

            movementRepository.save(movement);
        }

        request.setStatus(RequestStatus.COMPLETED);
        request.setExecutedAt(LocalDateTime.now());

        Request updatedRequest = requestRepository.save(request);
        return requestMapper.toResponseDto(updatedRequest);
    }

    @Override
    public RequestWithItemsResponseDto getRequestById(UUID id) {
        Request request = requestRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("Request", id));
        return requestMapper.toResponseDto(request);
    }

    @Override
    public List<RequestWithItemsResponseDto> getRequestsByUserId(UUID userId) {
        // Проверяем, что пользователь существует
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        List<Request> requests = requestRepository.findByEmployeeIdWithItems(userId);
        return requests.stream()
                .map(requestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestWithItemsResponseDto> getRequestsByStatus(RequestStatus status) {
        List<Request> requests = requestRepository.findByStatusWithItems(status);
        return requests.stream()
                .map(requestMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}