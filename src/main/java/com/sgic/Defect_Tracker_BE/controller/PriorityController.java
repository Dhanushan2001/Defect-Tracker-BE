package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreatePriorityRequest;
import com.sgic.Defect_Tracker_BE.dto.PriorityDto;
import com.sgic.Defect_Tracker_BE.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getPriorities(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<PriorityDto> paginatedData = priorityService.getAllPriorities(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<PriorityDto> all = priorityService.getAllPrioritiesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PriorityDto>> getPriorityById(@PathVariable("id") Long id) {
        PriorityDto priority = priorityService.getPriorityById(id);
        return ResponseEntity.ok(ApiResponse.success(priority));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PriorityDto>> createPriority(@RequestBody CreatePriorityRequest request) {
        PriorityDto created = priorityService.createPriority(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Priority created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PriorityDto>> updatePriority(
            @PathVariable("id") Long id,
            @RequestBody CreatePriorityRequest request) {
        PriorityDto updated = priorityService.updatePriority(id, request);
        return ResponseEntity.ok(ApiResponse.success("Priority updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePriority(@PathVariable("id") Long id) {
        priorityService.deletePriority(id);
        return ResponseEntity.ok(ApiResponse.success("Priority deleted successfully", null));
    }
}
