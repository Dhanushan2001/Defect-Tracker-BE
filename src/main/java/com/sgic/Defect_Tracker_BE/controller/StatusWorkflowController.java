package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.SaveWorkflowRequest;
import com.sgic.Defect_Tracker_BE.dto.WorkflowStatusDto;
import com.sgic.Defect_Tracker_BE.dto.WorkflowTransitionDto;
import com.sgic.Defect_Tracker_BE.service.StatusWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatusWorkflowController {

    private final StatusWorkflowService statusWorkflowService;

    @GetMapping({
            "/api/v1/status/workflow",
            "/api/v1/status/workflow/",
            "/api/v1/workflow",
            "/api/v1/workflow/",
            "/api/v1/status-workflow",
            "/api/v1/status-workflow/"
    })
    public ResponseEntity<ApiResponse<List<WorkflowTransitionDto>>> getAllWorkflows() {
        List<WorkflowTransitionDto> workflows = statusWorkflowService.getAllWorkflows();
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    @PostMapping({
            "/api/v1/status/workflow",
            "/api/v1/status/workflow/",
            "/api/v1/workflow",
            "/api/v1/workflow/",
            "/api/v1/status-workflow",
            "/api/v1/status-workflow/"
    })
    public ResponseEntity<ApiResponse<List<WorkflowTransitionDto>>> saveWorkflow(
            @RequestBody SaveWorkflowRequest request) {
        List<WorkflowTransitionDto> workflows = statusWorkflowService.saveWorkflow(request);
        return ResponseEntity.ok(ApiResponse.success("Workflow saved successfully", workflows));
    }

    @DeleteMapping({
            "/api/v1/status/workflow/{id}",
            "/api/v1/status/workflow/{id}/",
            "/api/v1/workflow/{id}",
            "/api/v1/workflow/{id}/"
    })
    public ResponseEntity<ApiResponse<Void>> deleteWorkflow(@PathVariable("id") Long id) {
        statusWorkflowService.deleteWorkflowTransition(id);
        return ResponseEntity.ok(ApiResponse.success("Workflow transition deleted successfully", null));
    }

    @GetMapping({
            "/api/v1/status/{id}/next",
            "/api/v1/status/{id}/next/",
            "/api/v1/status-type/{id}/next",
            "/api/v1/status-type/{id}/next/"
    })
    public ResponseEntity<ApiResponse<List<WorkflowStatusDto>>> getNextStatuses(@PathVariable("id") Long id) {
        List<WorkflowStatusDto> nextStatuses = statusWorkflowService.getNextStatuses(id);
        return ResponseEntity.ok(ApiResponse.success(nextStatuses));
    }
}
