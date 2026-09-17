package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateDefectTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectTypeDto;
import com.sgic.Defect_Tracker_BE.service.DefectTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/defect-type")
@RequiredArgsConstructor
public class DefectTypeController {

    private final DefectTypeService defectTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getDefectTypes(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<DefectTypeDto> paginatedData = defectTypeService.getAllDefectTypes(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<DefectTypeDto> all = defectTypeService.getAllDefectTypesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DefectTypeDto>> getDefectTypeById(@PathVariable("id") Long id) {
        DefectTypeDto defectType = defectTypeService.getDefectTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(defectType));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DefectTypeDto>> createDefectType(@RequestBody CreateDefectTypeRequest request) {
        DefectTypeDto created = defectTypeService.createDefectType(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Defect type created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DefectTypeDto>> updateDefectType(
            @PathVariable("id") Long id,
            @RequestBody CreateDefectTypeRequest request) {
        DefectTypeDto updated = defectTypeService.updateDefectType(id, request);
        return ResponseEntity.ok(ApiResponse.success("Defect type updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDefectType(@PathVariable("id") Long id) {
        defectTypeService.deleteDefectType(id);
        return ResponseEntity.ok(ApiResponse.success("Defect type deleted successfully", null));
    }
}
