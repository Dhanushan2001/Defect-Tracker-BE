package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateStatusTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.StatusTypeDto;
import com.sgic.Defect_Tracker_BE.service.StatusTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status-type")
@RequiredArgsConstructor
public class StatusTypeController {

    private final StatusTypeService statusTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getStatusTypes(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<StatusTypeDto> paginatedData = statusTypeService.getAllStatusTypes(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<StatusTypeDto> all = statusTypeService.getAllStatusTypesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusTypeDto>> getStatusTypeById(@PathVariable("id") Long id) {
        StatusTypeDto statusType = statusTypeService.getStatusTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(statusType));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StatusTypeDto>> createStatusType(@RequestBody CreateStatusTypeRequest request) {
        StatusTypeDto created = statusTypeService.createStatusType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Status type created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusTypeDto>> updateStatusType(
            @PathVariable("id") Long id,
            @RequestBody CreateStatusTypeRequest request) {
        StatusTypeDto updated = statusTypeService.updateStatusType(id, request);
        return ResponseEntity.ok(ApiResponse.success("Status type updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStatusType(@PathVariable("id") Long id) {
        statusTypeService.deleteStatusType(id);
        return ResponseEntity.ok(ApiResponse.success("Status type deleted successfully", null));
    }
}
