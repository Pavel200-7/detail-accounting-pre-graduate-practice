package com.example.demo.application.impl;

import com.example.demo.application.serv.PartService;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.enums.MovementType;
import com.example.demo.infrastructure.exceptions.BusinessRuleException;
import com.example.demo.infrastructure.exceptions.EntityNotFoundException;
import com.example.demo.infrastructure.mappers.PartMovementMapper;
import com.example.demo.infrastructure.repositories.*;
import com.example.demo.infrastructure.security.SecurityService;
import com.example.demo.presentation.dto.request.CreateMovementDto;
import com.example.demo.presentation.dto.request.CreatePartDto;
import com.example.demo.presentation.dto.response.MovementListResponseDto;
import com.example.demo.presentation.dto.response.MovementResponseDto;
import com.example.demo.presentation.dto.response.PartWithStockResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final StockRepository stockRepository;
    private final MovementRepository movementRepository;
    private final UserRepository userRepository;
    private final PartMovementMapper mapper;
    private final SecurityService securityService;

    @Override
    @Transactional
    public PartWithStockResponseDto createPart(CreatePartDto dto) {
        // Проверка уникальности name
        if (partRepository.existsByName(dto.getName())) {
            throw new BusinessRuleException(
                    String.format("Part с name '%s' уже существует", dto.getName())
            );
        }

        // Проверка уникальности sku
        if (partRepository.existsBySku(dto.getSku())) {
            throw new BusinessRuleException(
                    String.format("Part с sku '%s' уже существует", dto.getSku())
            );
        }

        // Создаем Part
        Part part = mapper.toPartEntity(dto);
        part.setCreatedAt(LocalDateTime.now());
        Part savedPart = partRepository.save(part);

        // Создаем Stock с нулевыми значениями
        Stock stock = new Stock();
        stock.setPart(savedPart);
        stock.setQuantity(0);
        stock.setReservedQuantity(0);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);

        return mapper.toPartWithStockResponseDto(savedPart, stock);
    }

    @Override
    @Transactional
    public MovementResponseDto createMovement(CreateMovementDto dto) {
        // Находим Part
        Part part = partRepository.findById(dto.getPartId())
                .orElseThrow(() -> new EntityNotFoundException("Part", dto.getPartId()));

        // Находим Stock
        Stock stock = stockRepository.findByPartId(part.getId())
                .orElseThrow(() -> new EntityNotFoundException("Stock for Part", part.getId()));

        // Получаем текущего пользователя
        UUID currentUserId = securityService.getCurrentUserIdAsUUID();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User", currentUserId));

        int quantityBefore = stock.getQuantity();
        int quantityAfter;

        // Расчет нового количества в зависимости от типа движения
        if (dto.getType() == MovementType.RECEIPT) {
            quantityAfter = quantityBefore + dto.getQuantity();
        } else if (dto.getType() == MovementType.CONSUMPTION) {
            if (stock.getQuantity() < dto.getQuantity()) {
                throw new BusinessRuleException(
                        String.format("Недостаточно остатков. Доступно: %d, запрошено: %d",
                                stock.getQuantity(), dto.getQuantity())
                );
            }
            quantityAfter = quantityBefore - dto.getQuantity();
        } else {
            throw new BusinessRuleException("Неизвестный тип движения");
        }

        // Обновляем остатки в Stock
        stock.setQuantity(quantityAfter);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepository.save(stock);

        // Создаем Movement
        Movement movement = mapper.toMovementEntity(dto, part, currentUser);
        movement.setQuantityBefore(quantityBefore);
        movement.setQuantityAfter(quantityAfter);
        movement.setPerformedAt(LocalDateTime.now());

        Movement savedMovement = movementRepository.save(movement);

        return mapper.toMovementResponseDto(savedMovement, part, currentUser);
    }

    @Override
    public PartWithStockResponseDto getPartById(UUID id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part", id));

        Stock stock = stockRepository.findByPartId(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock for Part", id));

        return mapper.toPartWithStockResponseDto(part, stock);
    }

    @Override
    public List<PartWithStockResponseDto> getAllParts() {
        List<Part> parts = partRepository.findAll();

        return parts.stream()
                .map(part -> {
                    Stock stock = stockRepository.findByPartId(part.getId())
                            .orElse(null);
                    return mapper.toPartWithStockResponseDto(part, stock);
                })
                .collect(Collectors.toList());
    }

    @Override
    public MovementListResponseDto getPartWithMovements(UUID id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part", id));

        Stock stock = stockRepository.findByPartId(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock for Part", id));

        List<Movement> movements = movementRepository.findByPartIdOrderByPerformedAtDesc(id);

        List<MovementResponseDto> movementDtos = movements.stream()
                .map(movement -> {
                    User performedBy = movement.getPerformedBy();
                    return mapper.toMovementResponseDto(movement, part, performedBy);
                })
                .collect(Collectors.toList());

        return MovementListResponseDto.builder()
                .part(mapper.toPartWithStockResponseDto(part, stock))
                .movements(movementDtos)
                .build();
    }
}