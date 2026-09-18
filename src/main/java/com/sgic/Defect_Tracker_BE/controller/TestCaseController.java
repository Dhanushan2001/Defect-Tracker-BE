package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.TestCasePayloadDto;
import com.sgic.Defect_Tracker_BE.dto.TestCaseResponseDto;
import com.sgic.Defect_Tracker_BE.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @PostMapping({"/api/v1/sub-module/{subModuleId}/test-case", "/api/v1/sub-module/{subModuleId}/test-case/"})
    public ResponseEntity<ApiResponse<TestCaseResponseDto>> createTestCaseForSubModule(
            @PathVariable("subModuleId") Long subModuleId,
            @RequestBody TestCasePayloadDto payload) {
        TestCaseResponseDto created = testCaseService.createTestCase(subModuleId, payload);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Test case created successfully", created));
    }

    @PostMapping({"/api/v1/test-case", "/api/v1/test-case/"})
    public ResponseEntity<ApiResponse<TestCaseResponseDto>> createTestCase(
            @RequestBody TestCasePayloadDto payload) {
        TestCaseResponseDto created = testCaseService.createTestCase(payload != null ? payload.getSubModuleId() : null, payload);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Test case created successfully", created));
    }

    @GetMapping({"/api/v1/sub-module/{subModuleId}/test-case", "/api/v1/sub-module/{subModuleId}/test-case/"})
    public ResponseEntity<ApiResponse<List<TestCaseResponseDto>>> getTestCasesBySubModule(
            @PathVariable("subModuleId") Long subModuleId,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "defectTypeId", required = false) Long defectTypeId,
            @RequestParam(name = "severityId", required = false) Long severityId) {
        List<TestCaseResponseDto> list = testCaseService.getTestCasesBySubModule(subModuleId, description, defectTypeId, severityId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({
            "/api/v1/sub-module/{subModuleId}/test-case/{id}",
            "/api/v1/sub-module/{subModuleId}/test-case/{id}/",
            "/api/v1/test-case/{id}",
            "/api/v1/test-case/{id}/"
    })
    public ResponseEntity<ApiResponse<TestCaseResponseDto>> getTestCaseById(
            @PathVariable(name = "subModuleId", required = false) Long subModuleId,
            @PathVariable("id") Long id) {
        TestCaseResponseDto dto = testCaseService.getTestCaseById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PutMapping({
            "/api/v1/sub-module/{subModuleId}/test-case/{id}",
            "/api/v1/sub-module/{subModuleId}/test-case/{id}/",
            "/api/v1/test-case/{id}",
            "/api/v1/test-case/{id}/"
    })
    public ResponseEntity<ApiResponse<TestCaseResponseDto>> updateTestCase(
            @PathVariable(name = "subModuleId", required = false) Long subModuleId,
            @PathVariable("id") Long id,
            @RequestBody TestCasePayloadDto payload) {
        TestCaseResponseDto updated = testCaseService.updateTestCase(id, payload);
        return ResponseEntity.ok(ApiResponse.success("Test case updated successfully", updated));
    }

    @DeleteMapping({
            "/api/v1/sub-module/{subModuleId}/test-case/{id}",
            "/api/v1/sub-module/{subModuleId}/test-case/{id}/",
            "/api/v1/test-case/{id}",
            "/api/v1/test-case/{id}/"
    })
    public ResponseEntity<ApiResponse<Void>> deleteTestCase(
            @PathVariable(name = "subModuleId", required = false) Long subModuleId,
            @PathVariable("id") Long id) {
        testCaseService.deleteTestCase(id);
        return ResponseEntity.ok(ApiResponse.success("Test case deleted successfully", null));
    }

    @GetMapping({"/api/v1/module/{moduleId}/test-case", "/api/v1/module/{moduleId}/test-case/", "/api/v1/module/{moduleId}/test-cases"})
    public ResponseEntity<ApiResponse<List<TestCaseResponseDto>>> getTestCasesByModule(
            @PathVariable("moduleId") Long moduleId) {
        List<TestCaseResponseDto> list = testCaseService.getTestCasesByModuleId(moduleId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({"/api/v1/project/{projectId}/test-case", "/api/v1/project/{projectId}/test-case/", "/api/v1/project/{projectId}/test-cases"})
    public ResponseEntity<ApiResponse<List<TestCaseResponseDto>>> getTestCasesByProject(
            @PathVariable("projectId") Long projectId) {
        List<TestCaseResponseDto> list = testCaseService.getTestCasesByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({"/api/v1/test-case", "/api/v1/test-case/"})
    public ResponseEntity<ApiResponse<List<TestCaseResponseDto>>> getAllTestCases(
            @RequestParam(name = "projectId", required = false) Long projectId,
            @RequestParam(name = "moduleId", required = false) Long moduleId,
            @RequestParam(name = "subModuleId", required = false) Long subModuleId,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "defectTypeId", required = false) Long defectTypeId,
            @RequestParam(name = "severityId", required = false) Long severityId) {

        List<TestCaseResponseDto> list;
        if (subModuleId != null) {
            list = testCaseService.getTestCasesBySubModule(subModuleId, description, defectTypeId, severityId);
        } else if (moduleId != null) {
            list = testCaseService.getTestCasesByModuleId(moduleId);
        } else if (projectId != null) {
            list = testCaseService.getTestCasesByProjectId(projectId);
        } else {
            list = testCaseService.getAllTestCases();
        }
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
