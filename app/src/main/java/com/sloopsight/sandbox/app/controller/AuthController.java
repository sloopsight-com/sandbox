package com.sloopsight.sandbox.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sloopsight.sandbox.app.config.JwtUtils;
import com.sloopsight.sandbox.app.dto.request.AuthConfigRequest;
import com.sloopsight.sandbox.app.dto.request.LoginRequest;
import com.sloopsight.sandbox.app.dto.response.JwtResponse;
import com.sloopsight.sandbox.app.services.AuthConfigService;
import com.sloopsight.sandbox.app.services.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthConfigService authConfigService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Save provided config", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/config/{name}")
    public ResponseEntity<?> updateConfig(@PathVariable("name") String name, @Valid @RequestBody AuthConfigRequest authConfig) {
        return ResponseEntity.of(authConfigService.save(name, authConfig));
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        return "ok";
    }

    @Operation(summary = "Read provided config", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/config/{id}")
    public ResponseEntity<?> readConfig(@PathVariable("id") String name) {
        return ResponseEntity.of(authConfigService.get(name));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/signedIn")
    public ResponseEntity<?> isLoggedId() {
        {
            if (SecurityContextHolder.getContext() != null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication.getPrincipal() != null && authentication.isAuthenticated()) {

                    return ResponseEntity.ok().body(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                }
            }
        }
        return ResponseEntity.status(401).build();

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

}
