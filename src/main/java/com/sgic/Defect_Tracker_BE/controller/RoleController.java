package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateRoleRequest;
import com.sgic.Defect_Tracker_BE.dto.RoleDto;
import com.sgic.Defect_Tracker_BE.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getRoles(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<RoleDto> paginatedData = roleService.getAllRoles(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<RoleDto> all = roleService.getAllRolesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable("id") Long id) {
        RoleDto role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success(role));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@RequestBody CreateRoleRequest request) {
        RoleDto created = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Role created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(
            @PathVariable("id") Long id,
            @RequestBody CreateRoleRequest request) {
        RoleDto updated = roleService.updateRole(id, request);
        return ResponseEntity.ok(ApiResponse.success("Role updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }
}
