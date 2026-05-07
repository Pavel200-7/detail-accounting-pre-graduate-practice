package com.example.demo.presentation.dto.request;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String refreshToken;
}