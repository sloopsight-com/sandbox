package com.sloopsight.sandbox.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sloopsight.sandbox.app.dto.request.EndpointRequest;
import com.sloopsight.sandbox.app.dto.request.LogicRequest;
import com.sloopsight.sandbox.app.entity.Endpoint;
import com.sloopsight.sandbox.app.services.EndpointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class EndpointController {

    @Autowired
    private EndpointService endpointService;

    @Operation(summary = "Read single endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/endpoint/{endpointId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Endpoint> findOne(@PathVariable("endpointId") Long endpointId) {
        return ResponseEntity.of(endpointService.findOne(endpointId));

    }

    @Operation(summary = "Save endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/endpoint")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Endpoint> save(@RequestBody EndpointRequest endpoint) {
        return ResponseEntity.of(endpointService.save(endpoint));
    }

    @PutMapping("/endpoint/{id}/logic")
    @Operation(summary = "Update endpoint logic", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Endpoint> update(@PathVariable("id") Long id, @RequestBody LogicRequest logic) {
        return ResponseEntity.of(endpointService.updateLogic(id, logic.getLogic()));

    }

    @DeleteMapping("/endpoint/{endpointId}")
    @Operation(summary = "Update endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Endpoint> update(@PathVariable("endpointId") Long endpointId) {

        return ResponseEntity.of(endpointService.delete(endpointId));

    }
}
