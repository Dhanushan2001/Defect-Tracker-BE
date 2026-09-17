package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateReleaseTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseTypeDto;
import com.sgic.Defect_Tracker_BE.service.ReleaseTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/release-type")
@RequiredArgsConstructor
public class ReleaseTypeController {

    private final ReleaseTypeService releaseTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getReleaseTypes(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<ReleaseTypeDto> paginatedData = releaseTypeService.getAllReleaseTypes(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<ReleaseTypeDto> all = releaseTypeService.getAllReleaseTypesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReleaseTypeDto>> getReleaseTypeById(@PathVariable("id") Long id) {
        ReleaseTypeDto releaseType = releaseTypeService.getReleaseTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(releaseType));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReleaseTypeDto>> createReleaseType(@RequestBody CreateReleaseTypeRequest request) {
        ReleaseTypeDto created = releaseTypeService.createReleaseType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Release type created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReleaseTypeDto>> updateReleaseType(
            @PathVariable("id") Long id,
            @RequestBody CreateReleaseTypeRequest request) {
        ReleaseTypeDto updated = releaseTypeService.updateReleaseType(id, request);
        return ResponseEntity.ok(ApiResponse.success("Release type updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReleaseType(@PathVariable("id") Long id) {
        releaseTypeService.deleteReleaseType(id);
        return ResponseEntity.ok(ApiResponse.success("Release type deleted successfully", null));
    }
}
