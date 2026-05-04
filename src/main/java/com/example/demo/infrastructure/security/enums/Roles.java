package com.example.demo.infrastructure.security.enums;

public enum Roles {
    EMPLOYEE("employee"),        // рядовой сотрудник
    MANAGER("manager"),         // руководитель
    STOREKEEPER("storekeeper"),     // кладовщик/завскладом
    ADMIN("admin");           // администратор системы

    public final String name;

    private Roles(String name) {
        this.name = name;
    }
}