package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.CreateCommentRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectCommentDto;
import com.sgic.Defect_Tracker_BE.service.DefectCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DefectCommentController {

    private final DefectCommentService defectCommentService;

    @PostMapping({"/api/v1/comment", "/api/v1/comment/"})
    public ResponseEntity<ApiResponse<DefectCommentDto>> createComment(
            @RequestBody CreateCommentRequest request) {
        DefectCommentDto created = defectCommentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Comment added successfully", created));
    }

    @PostMapping({"/api/v1/defect/{defectId}/comment", "/api/v1/defect/{defectId}/comment/"})
    public ResponseEntity<ApiResponse<DefectCommentDto>> createCommentForDefect(
            @PathVariable("defectId") Long defectId,
            @RequestBody CreateCommentRequest request) {
        if (request.getDefectId() == null) {
            request.setDefectId(defectId);
        }
        DefectCommentDto created = defectCommentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Comment added successfully", created));
    }

    @GetMapping({"/api/v1/comment/defect/{defectId}", "/api/v1/comment/defect/{defectId}/",
                  "/api/v1/defect/{defectId}/comment", "/api/v1/defect/{defectId}/comment/"})
    public ResponseEntity<ApiResponse<List<DefectCommentDto>>> getCommentsByDefect(
            @PathVariable("defectId") Long defectId) {
        List<DefectCommentDto> comments = defectCommentService.getCommentsByDefectId(defectId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }
}
