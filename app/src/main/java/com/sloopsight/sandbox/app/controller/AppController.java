package com.sloopsight.sandbox.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1")
public class AppController {

    @Operation(summary = "List all available app", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/app")
    @PreAuthorize("hasRole('MODERATOR')")
    public String get() {
        return "Moderator Board.";
    }
}
