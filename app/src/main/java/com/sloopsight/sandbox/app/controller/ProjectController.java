package com.sloopsight.sandbox.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.sloopsight.sandbox.app.dto.request.ProjectRequest;
import com.sloopsight.sandbox.app.dto.response.Member;
import com.sloopsight.sandbox.app.entity.Endpoint;
import com.sloopsight.sandbox.app.entity.Project;
import com.sloopsight.sandbox.app.services.EndpointService;
import com.sloopsight.sandbox.app.services.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EndpointService endpointService;

    @Operation(summary = "List all projects", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/project")
    public Page<Project> projects(@RequestParam("page") int page, @RequestParam("size") int size) throws InterruptedException {
        return projectService.find(SecurityContextHolder.getContext().getAuthentication().getName(), page, size);
    }

    @Operation(summary = "List all endpoints", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/project/{projectId}/endpoint")
    public Page<Endpoint> endpoints(@PathVariable("projectId") Long projectId, @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return endpointService.find(projectId, page, size);
    }

    @Operation(summary = "Read single project", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> findOne(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.of(projectService.findOne(projectId));

    }

    @Operation(summary = "Read available member", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/project/{projectId}/available/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Member>> findAvailableMembers(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.getAvailableMembers(projectId));
    }

    @Operation(summary = "Read existing member", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/project/{projectId}/existing/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Member>> findExistingMembers(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.getExisitingMembers(projectId));
    }

    @GetMapping(value = "/project/{projectId}/docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> docs(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.of(projectService.getDocs(projectId));

    }

    @GetMapping(value = "/document/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> document(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.of(projectService.getDocs(projectId));

    }

    @Operation(summary = "Get swaggger Json", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/project/{projectId}/docs.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> docsJson(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.of(projectService.getDocs(projectId));

    }

    @Operation(summary = "Save project", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> save(@RequestBody ProjectRequest project) {
        try {
            return ResponseEntity.of(projectService.save(project));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/project/{projectId}")
    @Operation(summary = "Update project", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> update(@PathVariable("projectId") Long projectId, @RequestBody ProjectRequest project) {
        return ResponseEntity.of(projectService.update(projectId, project));

    }

    @DeleteMapping("/project/{projectId}")
    @Operation(summary = "Update project", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Project> update(@PathVariable("projectId") Long projectId) {

        return ResponseEntity.of(projectService.delete(projectId));

    }
}
