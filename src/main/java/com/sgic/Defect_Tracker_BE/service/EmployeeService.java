package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateEmployeeRequest;
import com.sgic.Defect_Tracker_BE.dto.EmployeeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    PaginatedData<EmployeeDto> getAllEmployees(Pageable pageable);

    List<EmployeeDto> getAllEmployeesList();

    EmployeeDto getEmployeeById(Long id);

    EmployeeDto createEmployee(CreateEmployeeRequest request);

    EmployeeDto updateEmployee(Long id, CreateEmployeeRequest request);

    EmployeeDto updateEmployeeStatus(Long id, Boolean status);

    void deleteEmployee(Long id);
}
