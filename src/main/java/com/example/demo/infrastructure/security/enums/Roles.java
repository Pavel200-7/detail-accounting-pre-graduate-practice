package com.example.demo.infrastructure.security.enums;

public enum Roles {
    EMPLOYEE("EMPLOYEE"),        // рядовой сотрудник
    MANAGER("MANAGER"),         // руководитель
    STOREKEEPER("STOREKEEPER"),     // кладовщик/завскладом
    ADMIN("ADMIN");           // администратор системы

    public final String name;

    private Roles(String name) {
        this.name = name;
    }
}