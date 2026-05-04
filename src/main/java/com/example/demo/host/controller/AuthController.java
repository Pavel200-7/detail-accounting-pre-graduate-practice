package com.example.demo.host.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Аутентификация", description = "API для входа в систему и получения JWT токена")
public class AuthController {

    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя. Возвращает JWT токен.")
    @PostMapping("/login")
    public ResponseEntity<String> login() {
        // TODO: Реализовать реальную проверку пароля и генерацию токена
        return ResponseEntity.ok("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJRb01ROUhVUkZKWEJtamNscjVPSk83cURlejk0OVN0NW9jdXNWVHJzamFrIn0.eyJleHAiOjE3NzY3NzMzODEsImlhdCI6MTc3Njc3MTU4MSwianRpIjoib25ydHJvOmY5YjA5OTgxLWM3ZGEtYTMzMS1lYjVkLTM5YjljNzcxYWEwYyIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3QvYXV0aC9yZWFsbXMvb0F1dGgiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOGQ3MGYwNjItNjBhYi00OTlhLWExOGQtNzFkMzYwMzE2YjkzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibXljbGllbnQiLCJzaWQiOiJOQmFKejlGYkU1QnFHVC1wTDk4MlJ3aFgiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtb2F1dGgiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyMSBVc2VyMSIsInNwcmluZ19zZWNfcm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtb2F1dGgiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlcjEiLCJnaXZlbl9uYW1lIjoiVXNlcjEiLCJmYW1pbHlfbmFtZSI6IlVzZXIxIiwiZW1haWwiOiJ1c2VyMUB1c2VyMSJ9.ZqUxCJIU-jjZAQcG5cE25kYItqW4mUXyr5BN9TkAQgQjdRWUo9m_GVcjkzOfRwH71F2Kz1MGUYLNc1-JX8N7sNXKRNIhMoWS8eaYi6F4udYIXfGnuGC6kwGhcmCsmZAS9_wzXcmLwlkMe0jRNfbtBVYPHO-SgQZxX6DQthTPaNzGRXoJ2VIFYYsjT74wesJN_uXpI4djBAPB-2LGWDupq6gWA3XrAYeCRGESVVK7zLbhrJrM2UwYdtMp2MyK4zSWxM3jWjN0Xh0TOOQrXvljgGruimUWVOEYBR5IfAL8ObiGWP4uml2b-apT_ecbNYJnxjY1JxB7qb18qS2i4trp4g");
    }

    @Operation(summary = "Регистрация пользователя", description = "Создание нового пользователя. Требует прав ADMIN.")
    @PostMapping("/register")
    public ResponseEntity<Void> register() {
        return ResponseEntity.ok().build();
    }
}