package com.example.demo.infrastructure.utils;

import com.example.demo.presentation.dto.request.CreateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class JwtHelper {

    public CreateUserDto extractUserFromToken(String accessToken) {
        CreateUserDto createUserDto = new CreateUserDto();

        try {
            String[] parts = accessToken.split("\\.");
            if (parts.length > 1) {
                String payload = new String(Base64.getDecoder().decode(parts[1]));

                String sub = extractClaim(payload, "sub");
                if (sub != null) {
                    createUserDto.setId(UUID.fromString(sub));
                }

                String email = extractClaim(payload, "email");
                createUserDto.setEmail(email != null ? email : "unknown@example.com");

                String name = extractClaim(payload, "name");
                if (name == null || name.isEmpty()) {
                    name = extractClaim(payload, "preferred_username");
                }
                if (name == null || name.isEmpty()) {
                    name = createUserDto.getEmail();
                }
                createUserDto.setFullName(name);

                String phone = extractClaim(payload, "phone_number");
                createUserDto.setPhone(phone != null ? phone : "");

                log.info("Extracted user from token: id={}, email={}, name={}, phone={}",
                        createUserDto.getId(), createUserDto.getEmail(),
                        createUserDto.getFullName(), createUserDto.getPhone());
            }
        } catch (Exception e) {
            log.error("Failed to extract user from token: {}", e.getMessage());
            if (createUserDto.getId() == null) {
                createUserDto.setId(UUID.randomUUID());
            }
            if (createUserDto.getEmail() == null) {
                createUserDto.setEmail("unknown@example.com");
            }
            if (createUserDto.getFullName() == null) {
                createUserDto.setFullName("Unknown User");
            }
            if (createUserDto.getPhone() == null) {
                createUserDto.setPhone("");
            }
        }

        return createUserDto;
    }

    /**
     * Извлекает конкретный claim из payload JWT
     */
    public String extractClaim(String payload, String claimName) {
        String searchPattern = "\"" + claimName + "\":\"";
        int startIndex = payload.indexOf(searchPattern);

        if (startIndex != -1) {
            startIndex += searchPattern.length();
            int endIndex = payload.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return payload.substring(startIndex, endIndex);
            }
        }

        searchPattern = "\"" + claimName + "\":";
        startIndex = payload.indexOf(searchPattern);
        if (startIndex != -1) {
            startIndex += searchPattern.length();
            int endIndex = payload.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = payload.indexOf("}", startIndex);
            }
            if (endIndex != -1) {
                String value = payload.substring(startIndex, endIndex);
                // Убираем кавычки, если есть
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }
        }

        return null;
    }

    public UUID extractUserId(String accessToken) {
        try {
            String[] parts = accessToken.split("\\.");
            if (parts.length > 1) {
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                String sub = extractClaim(payload, "sub");
                if (sub != null) {
                    return UUID.fromString(sub);
                }
            }
        } catch (Exception e) {
            log.error("Failed to extract user id from token: {}", e.getMessage());
        }
        return UUID.randomUUID();
    }

    public String extractEmail(String accessToken) {
        try {
            String[] parts = accessToken.split("\\.");
            if (parts.length > 1) {
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                return extractClaim(payload, "email");
            }
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
        }
        return null;
    }
}