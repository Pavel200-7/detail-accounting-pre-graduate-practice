package com.example.demo.domain.enums;

public enum RequestStatus {
    DRAFT,           // черновик
    PENDING,         // на согласовании
    APPROVED,        // утверждена
    REJECTED,        // отклонена
    COMPLETED,       // выполнена
    NEED_PURCHASE    // требует закупки (нет на складе)
}