package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.*;
import com.sgic.Defect_Tracker_BE.service.ReleaseTestAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReleaseTestAllocationController {

    private final ReleaseTestAllocationService releaseTestAllocationService;

    // 1:1 Mode: allocate single test case to single release via PathVariable
    @PostMapping({
            "/api/v1/release/{releaseId}/test-case/{testCaseId}",
            "/api/v1/release/{releaseId}/test-case/{testCaseId}/"
    })
    public ResponseEntity<ApiResponse<ReleaseTestAllocationResponseDto>> allocateTestCaseToRelease(
            @PathVariable("releaseId") Long releaseId,
            @PathVariable("testCaseId") Long testCaseId) {
        ReleaseTestAllocationResponseDto dto = releaseTestAllocationService.allocateTestCaseToRelease(releaseId, testCaseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Test case allocated to release successfully", dto));
    }

    // 1:1 Mode: allocate single test case to release via RequestBody
    @PostMapping({
            "/api/v1/release/{releaseId}/test-case",
            "/api/v1/release/{releaseId}/test-case/"
    })
    public ResponseEntity<ApiResponse<ReleaseTestAllocationResponseDto>> allocateTestCaseWithBody(
            @PathVariable("releaseId") Long releaseId,
            @RequestBody Map<String, Object> body) {
        Object tcIdObj = body.get("testCaseId") != null ? body.get("testCaseId") : body.get("testcaseId");
        if (tcIdObj == null) {
            tcIdObj = body.get("id");
        }
        Long testCaseId = tcIdObj != null ? Long.valueOf(tcIdObj.toString()) : null;
        ReleaseTestAllocationResponseDto dto = releaseTestAllocationService.allocateTestCaseToRelease(releaseId, testCaseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Test case allocated to release successfully", dto));
    }

    // 1:M Mode: allocate 1 test case to multiple releases
    @PostMapping({
            "/api/v1/release/test-case/allocate-multiple-releases",
            "/api/v1/release/test-case/allocate-multiple-releases/"
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> allocateTestCaseToMultipleReleases(
            @RequestBody MultipleReleasesAllocationRequest request) {
        Map<String, Object> result = releaseTestAllocationService.allocateTestCaseToMultipleReleases(
                request.getTestCaseId(), request.getReleaseIds());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // Bulk Mode: allocate multiple test cases to single release
    @PostMapping({
            "/api/v1/release/{releaseId}/test-case/bulk",
            "/api/v1/release/{releaseId}/test-case/bulk/"
    })
    public ResponseEntity<ApiResponse<List<ReleaseTestAllocationResponseDto>>> bulkAllocateTestCases(
            @PathVariable("releaseId") Long releaseId,
            @RequestBody BulkAllocateReleaseTestCasesRequest request) {
        List<ReleaseTestAllocationResponseDto> list = releaseTestAllocationService.bulkAllocateTestCases(
                releaseId, request.getTestCaseIds());
        return ResponseEntity.ok(ApiResponse.success("Bulk test case allocation succeeded", list));
    }

    // M:M Mode: allocate multiple test cases to multiple releases
    @PostMapping({
            "/api/v1/release/test-case/many-to-many",
            "/api/v1/release/test-case/many-to-many/"
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> allocateManyToMany(
            @RequestBody ManyToManyAllocationRequest request) {
        List<Map<String, Object>> result = releaseTestAllocationService.allocateManyToMany(
                request.getReleaseIds(), request.getTestCaseIds());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // Get test cases allocated to a release (with optional module and submodule filters)
    @GetMapping({
            "/api/v1/release/{releaseId}/test-case",
            "/api/v1/release/{releaseId}/test-case/"
    })
    public ResponseEntity<ApiResponse<List<ReleaseTestAllocationResponseDto>>> getReleaseAllocatedTestCases(
            @PathVariable("releaseId") Long releaseId,
            @RequestParam(name = "moduleId", required = false) Long moduleId,
            @RequestParam(name = "subModuleId", required = false) Long subModuleId,
            @RequestParam(name = "submoduleId", required = false) Long submoduleIdAlias) {
        Long effectiveSubModuleId = subModuleId != null ? subModuleId : submoduleIdAlias;
        List<ReleaseTestAllocationResponseDto> list = releaseTestAllocationService.getReleaseAllocatedTestCases(
                releaseId, moduleId, effectiveSubModuleId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    // Deallocate test case from release
    @DeleteMapping({
            "/api/v1/release/{releaseId}/test-case/{testCaseId}",
            "/api/v1/release/{releaseId}/test-case/{testCaseId}/"
    })
    public ResponseEntity<ApiResponse<Void>> deallocateTestCase(
            @PathVariable("releaseId") Long releaseId,
            @PathVariable("testCaseId") Long testCaseId) {
        releaseTestAllocationService.deallocateTestCase(releaseId, testCaseId);
        return ResponseEntity.ok(ApiResponse.success("Test case deallocated successfully from release", null));
    }

    // Get QA members allocated to project (for QA dropdown)
    @GetMapping({
            "/api/v1/project/{projectId}/qa-members",
            "/api/v1/project/{projectId}/qa-members/",
            "/api/v1/project-allocation/{projectId}/qa-members",
            "/api/v1/project-allocation/{projectId}/qa-members/"
    })
    public ResponseEntity<ApiResponse<List<ProjectQaMemberDto>>> getProjectQaMembers(
            @PathVariable("projectId") Long projectId) {
        List<ProjectQaMemberDto> members = releaseTestAllocationService.getProjectQaMembers(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project QA members retrieved successfully", members));
    }

    // Assign or reassign QA to test cases
    @PostMapping({
            "/api/v1/qa-allocation/bulk-assign",
            "/api/v1/qa-allocation/bulk-assign/",
            "/api/v1/release/{releaseId}/test-case/qa-assign",
            "/api/v1/release/{releaseId}/test-case/qa-assign/"
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> bulkAssignQa(
            @PathVariable(name = "releaseId", required = false) Long releaseId,
            @RequestBody BulkAssignQaRequest request) {
        Long effectiveReleaseId = releaseId != null ? releaseId : request.getReleaseId();
        Map<String, Object> result = releaseTestAllocationService.bulkAssignQa(
                effectiveReleaseId, request.resolveQaId(), request.resolveTestCaseIds());
        return ResponseEntity.ok(ApiResponse.success("QA member assigned successfully", result));
    }

    // Update execution status (PASS / FAIL / NOT_RUN)
    @PatchMapping({
            "/api/v1/release/{releaseId}/test-case/{testCaseId}/status",
            "/api/v1/release/{releaseId}/test-case/{testCaseId}/status/",
            "/api/v1/release/{releaseId}/test-case/{testCaseId}",
            "/api/v1/release/{releaseId}/test-case/{testCaseId}/"
    })
    public ResponseEntity<ApiResponse<ReleaseTestAllocationResponseDto>> updateExecutionStatus(
            @PathVariable("releaseId") Long releaseId,
            @PathVariable("testCaseId") Long testCaseId,
            @RequestBody(required = false) Map<String, Object> body) {
        String status = "PASS";
        if (body != null) {
            if (body.get("status") != null) {
                status = body.get("status").toString();
            } else if (body.get("testCaseStatus") != null) {
                status = body.get("testCaseStatus").toString();
            } else if (body.get("executionStatus") != null) {
                status = body.get("executionStatus").toString();
            }
        }
        ReleaseTestAllocationResponseDto result = releaseTestAllocationService.updateExecutionStatus(
                releaseId, testCaseId, status);
        return ResponseEntity.ok(ApiResponse.success("Execution status updated successfully", result));
    }

    @PatchMapping({
            "/api/v1/release-test-case/{allocationId}/status",
            "/api/v1/release-test-case/{allocationId}/status/"
    })
    public ResponseEntity<ApiResponse<ReleaseTestAllocationResponseDto>> updateAllocationExecutionStatus(
            @PathVariable("allocationId") Long allocationId,
            @RequestBody(required = false) Map<String, Object> body) {
        String status = "PASS";
        Long releaseId = null;
        if (body != null) {
            if (body.get("status") != null) {
                status = body.get("status").toString();
            } else if (body.get("testCaseStatus") != null) {
                status = body.get("testCaseStatus").toString();
            } else if (body.get("executionStatus") != null) {
                status = body.get("executionStatus").toString();
            }
            if (body.get("releaseId") != null) {
                releaseId = Long.valueOf(body.get("releaseId").toString());
            }
        }
        ReleaseTestAllocationResponseDto result = releaseTestAllocationService.updateExecutionStatus(
                releaseId != null ? releaseId : 0L, allocationId, status);
        return ResponseEntity.ok(ApiResponse.success("Execution status updated successfully", result));
    }
}
