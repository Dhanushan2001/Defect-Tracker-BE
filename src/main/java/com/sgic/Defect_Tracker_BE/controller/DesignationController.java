package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateDesignationRequest;
import com.sgic.Defect_Tracker_BE.dto.DesignationDto;
import com.sgic.Defect_Tracker_BE.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/designation")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getDesignations(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<DesignationDto> paginatedData = designationService.getAllDesignations(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<DesignationDto> all = designationService.getAllDesignationsList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DesignationDto>> getDesignationById(@PathVariable("id") Long id) {
        DesignationDto designation = designationService.getDesignationById(id);
        return ResponseEntity.ok(ApiResponse.success(designation));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DesignationDto>> createDesignation(@RequestBody CreateDesignationRequest request) {
        DesignationDto created = designationService.createDesignation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Designation created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DesignationDto>> updateDesignation(
            @PathVariable("id") Long id,
            @RequestBody CreateDesignationRequest request) {
        DesignationDto updated = designationService.updateDesignation(id, request);
        return ResponseEntity.ok(ApiResponse.success("Designation updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDesignation(@PathVariable("id") Long id) {
        designationService.deleteDesignation(id);
        return ResponseEntity.ok(ApiResponse.success("Designation deleted successfully", null));
    }
}
