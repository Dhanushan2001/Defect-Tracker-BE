package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.AvailableManagerDto;
import com.sgic.Defect_Tracker_BE.dto.CreateProjectRequest;
import com.sgic.Defect_Tracker_BE.dto.ProjectDto;
import com.sgic.Defect_Tracker_BE.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping({"/api/v1/project", "/api/v1/project/"})
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @GetMapping({"/api/v1/project/{id}", "/api/v1/project/{id}/"})
    public ResponseEntity<ApiResponse<ProjectDto>> getProjectById(@PathVariable("id") Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @PostMapping({"/api/v1/project", "/api/v1/project/"})
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(@RequestBody CreateProjectRequest request) {
        ProjectDto created = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Project created successfully", created));
    }

    @PutMapping({"/api/v1/project/{id}", "/api/v1/project/{id}/"})
    public ResponseEntity<ApiResponse<ProjectDto>> updateProject(
            @PathVariable("id") Long id,
            @RequestBody CreateProjectRequest request) {
        ProjectDto updated = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", updated));
    }

    @DeleteMapping({"/api/v1/project/{id}", "/api/v1/project/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", null));
    }

    @GetMapping({
            "/api/v1/designation/{designationId}/available-managers",
            "/api/v1/designation/{designationId}/available-managers/",
            "/api/v1/designation/{designationId}/available-managers/project/{projectId}",
            "/api/v1/designation/{designationId}/available-managers/project/{projectId}/"
    })
    public ResponseEntity<ApiResponse<List<AvailableManagerDto>>> getAvailableManagers(
            @PathVariable("designationId") Long designationId,
            @PathVariable(name = "projectId", required = false) Long projectId) {
        List<AvailableManagerDto> managers = projectService.getAvailableManagers(designationId);
        return ResponseEntity.ok(ApiResponse.success(managers));
    }
}
