package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.CreateModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.ModuleDto;
import com.sgic.Defect_Tracker_BE.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping({"/api/v1/project/{projectId}/module", "/api/v1/project/{projectId}/module/"})
    public ResponseEntity<ApiResponse<ModuleDto>> createModuleForProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody CreateModuleRequest request) {
        ModuleDto created = moduleService.createModule(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Module created successfully", created));
    }

    @PostMapping({"/api/v1/module", "/api/v1/module/"})
    public ResponseEntity<ApiResponse<ModuleDto>> createModule(
            @RequestBody CreateModuleRequest request) {
        ModuleDto created = moduleService.createModule(request.getProjectId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Module created successfully", created));
    }

    @GetMapping({"/api/v1/project/{projectId}/module", "/api/v1/project/{projectId}/module/"})
    public ResponseEntity<ApiResponse<List<ModuleDto>>> getModulesByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<ModuleDto> modules = moduleService.getModulesByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(modules));
    }

    @GetMapping({"/api/v1/module", "/api/v1/module/"})
    public ResponseEntity<ApiResponse<List<ModuleDto>>> getModules(
            @RequestParam(name = "projectId", required = false) Long projectId) {
        List<ModuleDto> modules;
        if (projectId != null) {
            modules = moduleService.getModulesByProjectId(projectId);
        } else {
            modules = moduleService.getAllModules();
        }
        return ResponseEntity.ok(ApiResponse.success(modules));
    }

    @GetMapping({"/api/v1/module/{id}", "/api/v1/module/{id}/", "/api/v1/project/{projectId}/module/{id}", "/api/v1/project/{projectId}/module/{id}/"})
    public ResponseEntity<ApiResponse<ModuleDto>> getModuleById(
            @PathVariable("id") Long id) {
        ModuleDto module = moduleService.getModuleById(id);
        return ResponseEntity.ok(ApiResponse.success(module));
    }

    @PutMapping({"/api/v1/project/{projectId}/module/{id}", "/api/v1/project/{projectId}/module/{id}/"})
    public ResponseEntity<ApiResponse<ModuleDto>> updateModuleWithProject(
            @PathVariable("projectId") Long projectId,
            @PathVariable("id") Long id,
            @RequestBody CreateModuleRequest request) {
        ModuleDto updated = moduleService.updateModule(projectId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Module updated successfully", updated));
    }

    @PutMapping({"/api/v1/module/{id}", "/api/v1/module/{id}/"})
    public ResponseEntity<ApiResponse<ModuleDto>> updateModule(
            @PathVariable("id") Long id,
            @RequestBody CreateModuleRequest request) {
        ModuleDto updated = moduleService.updateModule(null, id, request);
        return ResponseEntity.ok(ApiResponse.success("Module updated successfully", updated));
    }

    @DeleteMapping({"/api/v1/project/{projectId}/module/{id}", "/api/v1/project/{projectId}/module/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteModuleWithProject(
            @PathVariable("projectId") Long projectId,
            @PathVariable("id") Long id) {
        moduleService.deleteModule(projectId, id);
        return ResponseEntity.ok(ApiResponse.success("Module deleted successfully", null));
    }

    @DeleteMapping({"/api/v1/module/{id}", "/api/v1/module/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteModule(
            @PathVariable("id") Long id) {
        moduleService.deleteModule(null, id);
        return ResponseEntity.ok(ApiResponse.success("Module deleted successfully", null));
    }
}
