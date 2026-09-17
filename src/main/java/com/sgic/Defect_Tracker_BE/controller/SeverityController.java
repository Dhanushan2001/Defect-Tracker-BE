package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateSeverityRequest;
import com.sgic.Defect_Tracker_BE.dto.SeverityDto;
import com.sgic.Defect_Tracker_BE.service.SeverityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/severity")
@RequiredArgsConstructor
public class SeverityController {

    private final SeverityService severityService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getSeverities(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<SeverityDto> paginatedData = severityService.getAllSeverities(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<SeverityDto> all = severityService.getAllSeveritiesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SeverityDto>> getSeverityById(@PathVariable("id") Long id) {
        SeverityDto severity = severityService.getSeverityById(id);
        return ResponseEntity.ok(ApiResponse.success(severity));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SeverityDto>> createSeverity(@RequestBody CreateSeverityRequest request) {
        SeverityDto created = severityService.createSeverity(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Severity created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SeverityDto>> updateSeverity(
            @PathVariable("id") Long id,
            @RequestBody CreateSeverityRequest request) {
        SeverityDto updated = severityService.updateSeverity(id, request);
        return ResponseEntity.ok(ApiResponse.success("Severity updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSeverity(@PathVariable("id") Long id) {
        severityService.deleteSeverity(id);
        return ResponseEntity.ok(ApiResponse.success("Severity deleted successfully", null));
    }
}
