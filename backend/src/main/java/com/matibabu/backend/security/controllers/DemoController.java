package com.matibabu.backend.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the application";
    }

    @GetMapping("/api/user")
    public String userEndpoint() {
        return "You are authenticated as USER or ADMIN";
    }

    @GetMapping("/api/admin")
    public String adminEndpoint() {
        return "You are authenticated as ADMIN";
    }
}
