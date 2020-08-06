package com.sloopsight.sandbox.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sloopsight.sandbox.app.services.HintGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1")
public class HintController {

    @Autowired
    private HintGenerator hintGenerator;

    @Operation(summary = "List all hints", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hint")
    public ResponseEntity<?> hint() {
        return ResponseEntity.of(Optional.of(hintGenerator.get()));
    }
}
