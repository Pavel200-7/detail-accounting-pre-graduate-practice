package com.example.demo.infrastructure.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, Object id) {
        super(String.format("%s с id '%s' не найден", entityName, id));
    }
}
