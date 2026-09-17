package com.sgic.Defect_Tracker_BE.controller;

import com.sgic.Defect_Tracker_BE.common.response.ApiResponse;
import com.sgic.Defect_Tracker_BE.dto.EmployeeDto;
import com.sgic.Defect_Tracker_BE.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/bench", "/api/v1/bench/"})
@RequiredArgsConstructor
public class BenchController {

    private final EmployeeService employeeService;

    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getBenchEmployees() {
        List<EmployeeDto> allEmployees = employeeService.getAllEmployeesList();
        return ResponseEntity.ok(ApiResponse.success(allEmployees));
    }
}
