package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.CreateReleaseRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseDto;
import com.sgic.Defect_Tracker_BE.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @PostMapping({"/api/v1/project/{projectId}/release", "/api/v1/project/{projectId}/release/"})
    public ResponseEntity<ApiResponse<ReleaseDto>> createReleaseForProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody CreateReleaseRequest request) {
        ReleaseDto created = releaseService.createRelease(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Release created successfully", created));
    }

    @PostMapping({"/api/v1/release", "/api/v1/release/"})
    public ResponseEntity<ApiResponse<ReleaseDto>> createRelease(
            @RequestBody CreateReleaseRequest request) {
        ReleaseDto created = releaseService.createRelease(request.resolveProjectId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Release created successfully", created));
    }

    @GetMapping({"/api/v1/project/{projectId}/release", "/api/v1/project/{projectId}/release/"})
    public ResponseEntity<ApiResponse<List<ReleaseDto>>> getReleasesByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<ReleaseDto> releases = releaseService.getReleasesByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(releases));
    }

    @GetMapping({"/api/v1/project/{projectId}/release/active", "/api/v1/project/{projectId}/release/active/"})
    public ResponseEntity<ApiResponse<List<ReleaseDto>>> getActiveReleasesByProjectId(
            @PathVariable("projectId") Long projectId) {
        List<ReleaseDto> releases = releaseService.getActiveReleasesByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(releases));
    }

    @GetMapping({"/api/v1/release", "/api/v1/release/"})
    public ResponseEntity<ApiResponse<List<ReleaseDto>>> getReleases(
            @RequestParam(name = "projectId", required = false) Long projectId) {
        List<ReleaseDto> releases;
        if (projectId != null) {
            releases = releaseService.getReleasesByProjectId(projectId);
        } else {
            releases = releaseService.getAllReleases();
        }
        return ResponseEntity.ok(ApiResponse.success(releases));
    }

    @GetMapping({"/api/v1/release/{id}", "/api/v1/release/{id}/"})
    public ResponseEntity<ApiResponse<ReleaseDto>> getReleaseById(
            @PathVariable("id") Long id) {
        ReleaseDto release = releaseService.getReleaseById(id);
        return ResponseEntity.ok(ApiResponse.success(release));
    }

    @PutMapping({"/api/v1/release/{id}", "/api/v1/release/{id}/", "/api/v1/project/{projectId}/release/{id}", "/api/v1/project/{projectId}/release/{id}/"})
    public ResponseEntity<ApiResponse<ReleaseDto>> updateRelease(
            @PathVariable("id") Long id,
            @RequestBody CreateReleaseRequest request) {
        ReleaseDto updated = releaseService.updateRelease(id, request);
        return ResponseEntity.ok(ApiResponse.success("Release updated successfully", updated));
    }

    @PatchMapping({"/api/v1/release/{id}/status", "/api/v1/release/{id}/status/"})
    public ResponseEntity<ApiResponse<ReleaseDto>> updateReleaseStatus(
            @PathVariable("id") Long id,
            @RequestBody(required = false) java.util.Map<String, Object> body) {
        String status = "HOLD";
        if (body != null && body.get("status") != null) {
            status = body.get("status").toString();
        }
        ReleaseDto updated = releaseService.updateReleaseStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Release status updated successfully", updated));
    }

    @DeleteMapping({"/api/v1/release/{id}", "/api/v1/release/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteRelease(
            @PathVariable("id") Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.ok(ApiResponse.success("Release deleted successfully", null));
    }
}
