package com.example.demo.presentation.controllers;

import com.example.demo.infrastructure.security.SecurityService;
import com.example.demo.infrastructure.security.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final SecurityService securityService;


    @GetMapping("/claims")
    public Object getClaims() {
        log.info("is admin {}", securityService.isAdmin());
        log.info("is admin {}", securityService.hasRole(Roles.ADMIN.name));
        return securityService.getAllClaims();
    }


}
