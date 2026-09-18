package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.AllocateModuleLeaderRequest;
import com.sgic.Defect_Tracker_BE.dto.EligibleLeaderDto;
import com.sgic.Defect_Tracker_BE.dto.ModuleLeaderResponseDto;
import com.sgic.Defect_Tracker_BE.service.ModuleLeaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModuleLeaderController {

    private final ModuleLeaderService moduleLeaderService;

    @GetMapping({
            "/api/v1/project/{projectId}/eligible-leaders",
            "/api/v1/project/{projectId}/eligible-leaders/"
    })
    public ResponseEntity<ApiResponse<List<EligibleLeaderDto>>> getEligibleLeaders(
            @PathVariable("projectId") Long projectId) {
        List<EligibleLeaderDto> leaders = moduleLeaderService.getEligibleLeaders(projectId);
        return ResponseEntity.ok(ApiResponse.success("Eligible leaders retrieved successfully", leaders));
    }

    @PostMapping({
            "/api/v1/allocate-module-leader",
            "/api/v1/allocate-module-leader/"
    })
    public ResponseEntity<ApiResponse<ModuleLeaderResponseDto>> allocateModuleLeader(
            @RequestBody AllocateModuleLeaderRequest request) {
        ModuleLeaderResponseDto result = moduleLeaderService.allocateModuleLeader(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Module leader allocated successfully", result));
    }

    @DeleteMapping({
            "/api/v1/allocate-module-leader/{allocateModuleId}",
            "/api/v1/allocate-module-leader/{allocateModuleId}/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateModuleLeader(
            @PathVariable("allocateModuleId") Long allocateModuleId) {
        moduleLeaderService.deallocateModuleLeader(allocateModuleId);
        return ResponseEntity.ok(ApiResponse.success("Module leader deallocated successfully", null));
    }

    @DeleteMapping({
            "/api/v1/module/{moduleId}/allocated-leader",
            "/api/v1/module/{moduleId}/allocated-leader/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateModuleLeaderByModuleId(
            @PathVariable("moduleId") Long moduleId) {
        moduleLeaderService.deallocateModuleLeaderByModuleId(moduleId);
        return ResponseEntity.ok(ApiResponse.success("Module leader deallocated successfully", null));
    }

    @GetMapping({
            "/api/v1/module/{moduleId}/allocated-leader",
            "/api/v1/module/{moduleId}/allocated-leader/"
    })
    public ResponseEntity<ApiResponse<ModuleLeaderResponseDto>> getActiveLeaderByModuleId(
            @PathVariable("moduleId") Long moduleId) {
        ModuleLeaderResponseDto leader = moduleLeaderService.getActiveLeaderByModuleId(moduleId);
        return ResponseEntity.ok(ApiResponse.success(leader));
    }

    @GetMapping({
            "/api/v1/project/{projectId}/module-leaders",
            "/api/v1/project/{projectId}/module-leaders/"
    })
    public ResponseEntity<ApiResponse<List<ModuleLeaderResponseDto>>> getActiveLeadersByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<ModuleLeaderResponseDto> leaders = moduleLeaderService.getActiveLeadersByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(leaders));
    }
}
