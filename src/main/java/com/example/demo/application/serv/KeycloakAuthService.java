package com.example.demo.application.serv;

import com.example.demo.presentation.dto.request.LogoutRequestDto;
import com.example.demo.presentation.dto.request.RefreshTokenRequestDto;
import com.example.demo.presentation.dto.response.LoginResponseDto;
import reactor.core.publisher.Mono;

public interface KeycloakAuthService {
    String buildAuthorizationUrl();
    Mono<LoginResponseDto> exchangeCodeForTokens(String code);  // теперь возвращает LoginResponseDto
    Mono<LoginResponseDto> refreshToken(RefreshTokenRequestDto request);
    Mono<Void> logout(LogoutRequestDto request);
}