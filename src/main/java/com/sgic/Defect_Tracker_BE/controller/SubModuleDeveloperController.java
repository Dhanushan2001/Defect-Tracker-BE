package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.AllocateSubModuleDeveloperRequest;
import com.sgic.Defect_Tracker_BE.dto.EligibleLeaderDto;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDeveloperResponseDto;
import com.sgic.Defect_Tracker_BE.service.SubModuleDeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubModuleDeveloperController {

    private final SubModuleDeveloperService subModuleDeveloperService;

    @GetMapping({
            "/api/v1/project/{projectId}/eligible-developers",
            "/api/v1/project/{projectId}/eligible-developers/"
    })
    public ResponseEntity<ApiResponse<List<EligibleLeaderDto>>> getEligibleDevelopers(
            @PathVariable("projectId") Long projectId) {
        List<EligibleLeaderDto> developers = subModuleDeveloperService.getEligibleDevelopers(projectId);
        return ResponseEntity.ok(ApiResponse.success("Eligible developers retrieved successfully", developers));
    }

    @GetMapping({
            "/api/v1/sub-module/{subModuleId}/employee",
            "/api/v1/sub-module/{subModuleId}/employee/",
            "/api/v1/sub-module/{subModuleId}/employees",
            "/api/v1/sub-module/{subModuleId}/employees/"
    })
    public ResponseEntity<ApiResponse<List<SubModuleDeveloperResponseDto>>> getDevelopersBySubModuleId(
            @PathVariable("subModuleId") Long subModuleId) {
        List<SubModuleDeveloperResponseDto> developers = subModuleDeveloperService.getDevelopersBySubModuleId(subModuleId);
        return ResponseEntity.ok(ApiResponse.success("Submodule developers retrieved successfully", developers));
    }

    @PostMapping({
            "/api/v1/sub-module/{subModuleId}/employee/{employeeId}",
            "/api/v1/sub-module/{subModuleId}/employee/{employeeId}/"
    })
    public ResponseEntity<ApiResponse<SubModuleDeveloperResponseDto>> allocateDeveloperByPath(
            @PathVariable("subModuleId") Long subModuleId,
            @PathVariable("employeeId") Long employeeId) {
        SubModuleDeveloperResponseDto result = subModuleDeveloperService.allocateDeveloper(subModuleId, employeeId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Developer allocated to submodule successfully", result));
    }

    @PostMapping({
            "/api/v1/sub-module/{subModuleId}/employee",
            "/api/v1/sub-module/{subModuleId}/employee/",
            "/api/v1/allocate-submodule-developer",
            "/api/v1/allocate-submodule-developer/"
    })
    public ResponseEntity<ApiResponse<SubModuleDeveloperResponseDto>> allocateDeveloper(
            @PathVariable(value = "subModuleId", required = false) Long subModuleId,
            @RequestBody AllocateSubModuleDeveloperRequest request) {
        if (subModuleId != null) {
            request.setSubModuleId(subModuleId);
        }
        SubModuleDeveloperResponseDto result = subModuleDeveloperService.allocateDeveloper(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Developer allocated to submodule successfully", result));
    }

    @DeleteMapping({
            "/api/v1/sub-module/{subModuleId}/employee/{employeeId}",
            "/api/v1/sub-module/{subModuleId}/employee/{employeeId}/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateDeveloper(
            @PathVariable("subModuleId") Long subModuleId,
            @PathVariable("employeeId") Long employeeId) {
        subModuleDeveloperService.deallocateDeveloper(subModuleId, employeeId);
        return ResponseEntity.ok(ApiResponse.success("Developer deallocated from submodule successfully", null));
    }

    @DeleteMapping({
            "/api/v1/sub-module-developer/{id}",
            "/api/v1/sub-module-developer/{id}/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateById(
            @PathVariable("id") Long id) {
        subModuleDeveloperService.deallocateById(id);
        return ResponseEntity.ok(ApiResponse.success("Developer deallocated from submodule successfully", null));
    }

    @GetMapping({
            "/api/v1/project/{projectId}/sub-module-developers",
            "/api/v1/project/{projectId}/sub-module-developers/"
    })
    public ResponseEntity<ApiResponse<List<SubModuleDeveloperResponseDto>>> getDevelopersByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<SubModuleDeveloperResponseDto> list = subModuleDeveloperService.getDevelopersByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project submodule developers retrieved successfully", list));
    }
}
