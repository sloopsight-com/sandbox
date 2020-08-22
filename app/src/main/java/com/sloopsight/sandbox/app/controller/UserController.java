package com.sloopsight.sandbox.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sloopsight.sandbox.app.dto.request.SignupRequest;
import com.sloopsight.sandbox.app.dto.request.UserUpdationRequest;
import com.sloopsight.sandbox.app.entity.Role;
import com.sloopsight.sandbox.app.entity.User;
import com.sloopsight.sandbox.app.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "List all users", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public Page<User> user(@RequestParam("page") int page, @RequestParam("size") int size) {
        return userService.findAll(page, size);
    }

    @Operation(summary = "Create user", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user")
    public ResponseEntity<?> create(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @Operation(summary = "Update user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,@Valid @RequestBody   UserUpdationRequest updationRequest) {
        return ResponseEntity.ok(userService.update(id, updationRequest));
    }

    @Operation(summary = "Get available roles for user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}/roles/available")
    public ResponseEntity<List<Role>> roles(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getAvailableRoles(id));
    }

    @Operation(summary = "Read user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> read(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
