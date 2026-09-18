package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationHistoryDto;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationPayloadDto;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationResponseDto;
import com.sgic.Defect_Tracker_BE.service.ProjectAllocationHistoryService;
import com.sgic.Defect_Tracker_BE.service.ProjectAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectAllocationController {

    private final ProjectAllocationService projectAllocationService;
    private final ProjectAllocationHistoryService projectAllocationHistoryService;

    @PostMapping({"/api/v1/project-allocation", "/api/v1/project-allocation/"})
    public ResponseEntity<ApiResponse<ProjectAllocationResponseDto>> allocate(
            @RequestBody ProjectAllocationPayloadDto payload) {
        ProjectAllocationResponseDto created = projectAllocationService.allocate(payload);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Project allocation created successfully", created));
    }

    @GetMapping({"/api/v1/project-allocation", "/api/v1/project-allocation/"})
    public ResponseEntity<ApiResponse<List<ProjectAllocationResponseDto>>> getAllAllocations(
            @RequestParam(name = "projectId", required = false) Long projectId) {
        if (projectId != null) {
            List<ProjectAllocationResponseDto> list = projectAllocationService.getAllocationsByProjectId(projectId);
            return ResponseEntity.ok(ApiResponse.success(list));
        }
        List<ProjectAllocationResponseDto> list = projectAllocationService.getAllAllocations();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({
            "/api/v1/project-allocation/{projectId}/employee",
            "/api/v1/project-allocation/{projectId}/employee/",
            "/api/v1/project-allocation/{projectId}/employees",
            "/api/v1/project-allocation/{projectId}/employees/"
    })
    public ResponseEntity<ApiResponse<List<ProjectAllocationResponseDto>>> getAllocationsByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<ProjectAllocationResponseDto> list = projectAllocationService.getAllocationsByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({
            "/api/v1/project-allocation/employee/{employeeId}",
            "/api/v1/project-allocation/employee/{employeeId}/"
    })
    public ResponseEntity<ApiResponse<List<ProjectAllocationResponseDto>>> getAllocationsByEmployeeId(
            @PathVariable("employeeId") Long employeeId) {
        List<ProjectAllocationResponseDto> list = projectAllocationService.getAllocationsByEmployeeId(employeeId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PutMapping({"/api/v1/project-allocation/{id}", "/api/v1/project-allocation/{id}/"})
    public ResponseEntity<ApiResponse<ProjectAllocationResponseDto>> updateAllocation(
            @PathVariable("id") Long id,
            @RequestBody ProjectAllocationPayloadDto payload) {
        ProjectAllocationResponseDto updated = projectAllocationService.updateAllocation(id, payload);
        return ResponseEntity.ok(ApiResponse.success("Project allocation updated successfully", updated));
    }

    @PutMapping({"/api/v1/project-allocation/employee/{id}/extend", "/api/v1/project-allocation/employee/{id}/extend/"})
    public ResponseEntity<ApiResponse<ProjectAllocationResponseDto>> extendAllocation(
            @PathVariable("id") Long id,
            @RequestBody ProjectAllocationPayloadDto payload) {
        ProjectAllocationResponseDto updated = projectAllocationService.updateAllocation(id, payload);
        return ResponseEntity.ok(ApiResponse.success("Project allocation extended successfully", updated));
    }

    @DeleteMapping({"/api/v1/project-allocation/{id}", "/api/v1/project-allocation/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deallocate(@PathVariable("id") Long id) {
        projectAllocationService.deallocate(id);
        return ResponseEntity.ok(ApiResponse.success("Project allocation removed successfully", null));
    }

    @DeleteMapping({
            "/api/v1/project-allocation/employee/{id}",
            "/api/v1/project-allocation/employee/{id}/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateByEmployeeOrAllocationId(@PathVariable("id") Long id) {
        List<ProjectAllocationResponseDto> empAllocs = projectAllocationService.getAllocationsByEmployeeId(id);
        if (!empAllocs.isEmpty()) {
            projectAllocationService.deallocateByEmployeeId(id);
        } else {
            projectAllocationService.deallocate(id);
        }
        return ResponseEntity.ok(ApiResponse.success("Project allocation removed successfully", null));
    }

    @DeleteMapping({
            "/api/v1/project-allocation/project/{projectId}/employee/{employeeId}",
            "/api/v1/project-allocation/project/{projectId}/employee/{employeeId}/",
            "/api/v1/project-allocation/{projectId}/employee/{employeeId}",
            "/api/v1/project-allocation/{projectId}/employee/{employeeId}/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateByProjectAndEmployee(
            @PathVariable("projectId") Long projectId,
            @PathVariable("employeeId") Long employeeId) {
        projectAllocationService.deallocateByProjectAndEmployee(projectId, employeeId);
        return ResponseEntity.ok(ApiResponse.success("Project allocation removed successfully", null));
    }

    @GetMapping({
            "/api/v1/project-allocation/{projectId}/employee_history",
            "/api/v1/project-allocation/{projectId}/employee_history/",
            "/api/v1/project-allocation/{projectId}/history",
            "/api/v1/project-allocation/{projectId}/history/",
            "/api/v1/project/{projectId}/allocation-history",
            "/api/v1/project/{projectId}/allocation-history/"
    })
    public ResponseEntity<ApiResponse<List<ProjectAllocationHistoryDto>>> getProjectAllocationHistory(
            @PathVariable("projectId") Long projectId) {
        List<ProjectAllocationHistoryDto> list = projectAllocationHistoryService.getHistoryByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({"/api/v1/project-allocation/history", "/api/v1/project-allocation/history/"})
    public ResponseEntity<ApiResponse<List<ProjectAllocationHistoryDto>>> getAllAllocationHistory(
            @RequestParam(name = "projectId", required = false) Long projectId,
            @RequestParam(name = "employeeId", required = false) Long employeeId) {
        List<ProjectAllocationHistoryDto> list;
        if (projectId != null) {
            list = projectAllocationHistoryService.getHistoryByProjectId(projectId);
        } else if (employeeId != null) {
            list = projectAllocationHistoryService.getHistoryByEmployeeId(employeeId);
        } else {
            list = projectAllocationHistoryService.getAllHistory();
        }
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
