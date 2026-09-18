package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.CreateSubModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDto;
import com.sgic.Defect_Tracker_BE.repository.SubModuleRepository;
import com.sgic.Defect_Tracker_BE.mapper.SubModuleMapper;
import com.sgic.Defect_Tracker_BE.service.SubModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubModuleController {

    private final SubModuleService subModuleService;
    private final SubModuleRepository subModuleRepository;
    private final SubModuleMapper subModuleMapper;

    @PostMapping({"/api/v1/module/{moduleId}/sub-module", "/api/v1/module/{moduleId}/sub-module/"})
    public ResponseEntity<ApiResponse<SubModuleDto>> createSubModuleForModule(
            @PathVariable("moduleId") Long moduleId,
            @RequestBody CreateSubModuleRequest request) {
        SubModuleDto created = subModuleService.createSubModule(moduleId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Submodule created successfully", created));
    }

    @PostMapping({"/api/v1/sub-module", "/api/v1/sub-module/"})
    public ResponseEntity<ApiResponse<SubModuleDto>> createSubModule(
            @RequestBody CreateSubModuleRequest request) {
        SubModuleDto created = subModuleService.createSubModule(request.getModuleId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Submodule created successfully", created));
    }

    @GetMapping({"/api/v1/module/{moduleId}/sub-module", "/api/v1/module/{moduleId}/sub-module/"})
    public ResponseEntity<ApiResponse<List<SubModuleDto>>> getSubModulesByModuleId(
            @PathVariable("moduleId") Long moduleId) {
        List<SubModuleDto> subModules = subModuleService.getSubModulesByModuleId(moduleId);
        return ResponseEntity.ok(ApiResponse.success(subModules));
    }

    @GetMapping({"/api/v1/project/{projectId}/sub-module", "/api/v1/project/{projectId}/sub-module/"})
    public ResponseEntity<ApiResponse<List<SubModuleDto>>> getSubModulesByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<SubModuleDto> subModules = subModuleService.getSubModulesByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(subModules));
    }

    @GetMapping({"/api/v1/sub-module/{id}", "/api/v1/sub-module/{id}/", "/api/v1/module/{moduleId}/sub-module/{id}", "/api/v1/module/{moduleId}/sub-module/{id}/"})
    public ResponseEntity<ApiResponse<SubModuleDto>> getSubModuleById(
            @PathVariable("id") Long id) {
        SubModuleDto subModule = subModuleService.getSubModuleById(id);
        return ResponseEntity.ok(ApiResponse.success(subModule));
    }

    @PutMapping({"/api/v1/module/{moduleId}/sub-module/{id}", "/api/v1/module/{moduleId}/sub-module/{id}/"})
    public ResponseEntity<ApiResponse<SubModuleDto>> updateSubModuleWithModule(
            @PathVariable("moduleId") Long moduleId,
            @PathVariable("id") Long id,
            @RequestBody CreateSubModuleRequest request) {
        SubModuleDto updated = subModuleService.updateSubModule(moduleId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Submodule updated successfully", updated));
    }

    @PutMapping({"/api/v1/sub-module/{id}", "/api/v1/sub-module/{id}/"})
    public ResponseEntity<ApiResponse<SubModuleDto>> updateSubModule(
            @PathVariable("id") Long id,
            @RequestBody CreateSubModuleRequest request) {
        SubModuleDto updated = subModuleService.updateSubModule(null, id, request);
        return ResponseEntity.ok(ApiResponse.success("Submodule updated successfully", updated));
    }

    @DeleteMapping({"/api/v1/module/{moduleId}/sub-module/{id}", "/api/v1/module/{moduleId}/sub-module/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteSubModuleWithModule(
            @PathVariable("moduleId") Long moduleId,
            @PathVariable("id") Long id) {
        subModuleService.deleteSubModule(moduleId, id);
        return ResponseEntity.ok(ApiResponse.success("Submodule deleted successfully", null));
    }

    @DeleteMapping({"/api/v1/sub-module/{id}", "/api/v1/sub-module/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteSubModule(
            @PathVariable("id") Long id) {
        subModuleService.deleteSubModule(null, id);
        return ResponseEntity.ok(ApiResponse.success("Submodule deleted successfully", null));
    }

    @PostMapping({"/api/v1/subModule/bulk-by-modules", "/api/v1/subModule/bulk-by-modules/"})
    public ResponseEntity<ApiResponse<List<SubModuleDto>>> getSubModulesByModules(
            @RequestBody List<Long> moduleIds) {
        if (moduleIds == null || moduleIds.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
        }
        List<SubModuleDto> dtos = subModuleMapper.toDtoList(subModuleRepository.findByModuleIdIn(moduleIds));
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }
}
