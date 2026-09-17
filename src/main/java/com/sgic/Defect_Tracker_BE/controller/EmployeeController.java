package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateEmployeeRequest;
import com.sgic.Defect_Tracker_BE.dto.EmployeeDto;
import com.sgic.Defect_Tracker_BE.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/employee", "/api/v1/employee/", "/api/v1/user", "/api/v1/user/"})
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<?>> getEmployees(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        if (page != null && size != null) {
            Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            PaginatedData<EmployeeDto> paginatedData = employeeService.getAllEmployees(pageable);
            return ResponseEntity.ok(ApiResponse.success(paginatedData));
        } else {
            List<EmployeeDto> all = employeeService.getAllEmployeesList();
            return ResponseEntity.ok(ApiResponse.success(all));
        }
    }

    @GetMapping({"/simple", "/simple/"})
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployeesSimple() {
        List<EmployeeDto> all = employeeService.getAllEmployeesList();
        return ResponseEntity.ok(ApiResponse.success(all));
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeById(@PathVariable("id") Long id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(@RequestBody CreateEmployeeRequest request) {
        EmployeeDto created = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Employee created successfully", created));
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody CreateEmployeeRequest request) {
        EmployeeDto updated = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    @RequestMapping(value = {"/{id}/status", "/{id}/status/"}, method = {RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.POST})
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployeeStatus(
            @PathVariable("id") Long id,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(name = "status", required = false) Boolean statusParam) {
        Boolean targetStatus = true;
        if (statusParam != null) {
            targetStatus = statusParam;
        } else if (body != null) {
            if (body.containsKey("status")) {
                Object val = body.get("status");
                if (val instanceof Boolean) {
                    targetStatus = (Boolean) val;
                } else {
                    targetStatus = "true".equalsIgnoreCase(String.valueOf(val)) || "active".equalsIgnoreCase(String.valueOf(val));
                }
            } else if (body.containsKey("userStatus")) {
                Object val = body.get("userStatus");
                targetStatus = "ACTIVE".equalsIgnoreCase(String.valueOf(val)) || "true".equalsIgnoreCase(String.valueOf(val));
            } else if (body.containsKey("isActive")) {
                Object val = body.get("isActive");
                if (val instanceof Boolean) {
                    targetStatus = (Boolean) val;
                } else {
                    targetStatus = "true".equalsIgnoreCase(String.valueOf(val)) || "active".equalsIgnoreCase(String.valueOf(val));
                }
            }
        }
        EmployeeDto updated = employeeService.updateEmployeeStatus(id, targetStatus);
        return ResponseEntity.ok(ApiResponse.success("Employee status updated successfully", updated));
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }
}
