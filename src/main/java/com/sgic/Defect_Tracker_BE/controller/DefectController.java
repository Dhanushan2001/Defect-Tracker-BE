package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.CreateDefectRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectDto;
import com.sgic.Defect_Tracker_BE.dto.DefectHistoryDto;
import com.sgic.Defect_Tracker_BE.service.DefectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DefectController {

    private final DefectService defectService;

    @PostMapping({"/api/v1/defect", "/api/v1/defect/"})
    public ResponseEntity<ApiResponse<DefectDto>> createDefect(
            @RequestBody CreateDefectRequest request) {
        DefectDto created = defectService.createDefect(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Defect created successfully", created));
    }

    @PutMapping({"/api/v1/defect/{id}", "/api/v1/defect/{id}/"})
    public ResponseEntity<ApiResponse<DefectDto>> updateDefect(
            @PathVariable("id") Long id,
            @RequestBody CreateDefectRequest request) {
        DefectDto updated = defectService.updateDefect(id, request);
        return ResponseEntity.ok(ApiResponse.success("Defect updated successfully", updated));
    }

    @GetMapping({"/api/v1/defect", "/api/v1/defect/"})
    public ResponseEntity<ApiResponse<List<DefectDto>>> getDefects(
            @RequestParam(name = "projectId", required = false) Long projectId,
            @RequestParam(name = "releaseId", required = false) Long releaseId) {
        List<DefectDto> list;
        if (projectId != null) {
            list = defectService.getDefectsByProjectId(projectId);
        } else if (releaseId != null) {
            list = defectService.getDefectsByReleaseId(releaseId);
        } else {
            list = defectService.getAllDefects();
        }
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({"/api/v1/project/{projectId}/defect", "/api/v1/project/{projectId}/defect/"})
    public ResponseEntity<ApiResponse<List<DefectDto>>> getDefectsByProject(
            @PathVariable("projectId") Long projectId) {
        List<DefectDto> list = defectService.getDefectsByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({"/api/v1/release/{releaseId}/defect", "/api/v1/release/{releaseId}/defect/"})
    public ResponseEntity<ApiResponse<List<DefectDto>>> getDefectsByRelease(
            @PathVariable("releaseId") Long releaseId) {
        List<DefectDto> list = defectService.getDefectsByReleaseId(releaseId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping({"/api/v1/defect/{id}", "/api/v1/defect/{id}/"})
    public ResponseEntity<ApiResponse<DefectDto>> getDefectById(
            @PathVariable("id") Long id) {
        DefectDto defect = defectService.getDefectById(id);
        return ResponseEntity.ok(ApiResponse.success(defect));
    }

    @GetMapping({"/api/v1/defect/{id}/history", "/api/v1/defect/{id}/history/"})
    public ResponseEntity<ApiResponse<List<DefectHistoryDto>>> getDefectHistory(
            @PathVariable("id") Long id) {
        List<DefectHistoryDto> history = defectService.getDefectHistory(id);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @DeleteMapping({"/api/v1/defect/{id}", "/api/v1/defect/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteDefect(
            @PathVariable("id") Long id) {
        defectService.deleteDefect(id);
        return ResponseEntity.ok(ApiResponse.success("Defect deleted successfully", null));
    }
}
