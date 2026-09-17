package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateEmployeeRequest;
import com.sgic.Defect_Tracker_BE.dto.EmployeeDto;
import com.sgic.Defect_Tracker_BE.entity.Designation;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.mapper.EmployeeMapper;
import com.sgic.Defect_Tracker_BE.repository.DesignationRepository;
import com.sgic.Defect_Tracker_BE.repository.EmployeeRepository;
import com.sgic.Defect_Tracker_BE.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;
    private final EmployeeMapper employeeMapper;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern WHATSAPP_PATTERN =
            Pattern.compile("^\\d{10}$");

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<EmployeeDto> getAllEmployees(Pageable pageable) {
        Page<Employee> page = employeeRepository.findAll(pageable);
        return PaginatedData.<EmployeeDto>builder()
                .content(employeeMapper.toDtoList(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .size(page.getSize())
                .number(page.getNumber())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployeesList() {
        return employeeMapper.toDtoList(employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        validateEmployeeRequest(request);

        String email = request.getEmail().trim().toLowerCase();
        String contactNo = request.getContactNo().trim();
        String firstName = request.getFirstName().trim();
        String lastName = request.getLastName().trim();

        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Employee with email '" + email + "' already exists");
        }

        if (employeeRepository.existsByContactNo(contactNo)) {
            throw new DuplicateResourceException("Employee with WhatsApp number '" + contactNo + "' already exists");
        }

        if (employeeRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)) {
            throw new DuplicateResourceException("Employee with name '" + firstName + " " + lastName + "' already exists");
        }

        Designation designation = null;
        if (request.getDesignationId() != null) {
            designation = designationRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId()));
        }

        Employee employee = employeeMapper.toEntity(request, designation);
        if (employee.getAvailability() == null) {
            employee.setAvailability(100);
        }
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDto(saved);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, CreateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        validateEmployeeRequest(request);

        String email = request.getEmail().trim().toLowerCase();
        String contactNo = request.getContactNo().trim();
        String firstName = request.getFirstName().trim();
        String lastName = request.getLastName().trim();

        if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateResourceException("Employee with email '" + email + "' already exists");
        }

        if (employeeRepository.existsByContactNoAndIdNot(contactNo, id)) {
            throw new DuplicateResourceException("Employee with WhatsApp number '" + contactNo + "' already exists");
        }

        if (employeeRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(firstName, lastName, id)) {
            throw new DuplicateResourceException("Employee with name '" + firstName + " " + lastName + "' already exists");
        }

        Designation designation = null;
        if (request.getDesignationId() != null) {
            designation = designationRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + request.getDesignationId()));
        }

        employeeMapper.updateEntity(employee, request, designation);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toDto(updated);
    }

    @Override
    public EmployeeDto updateEmployeeStatus(Long id, Boolean status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setIsActive(status != null ? status : true);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toDto(updated);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    private void validateEmployeeRequest(CreateEmployeeRequest request) {
        if (request == null) {
            throw new BadRequestException("Employee request cannot be null");
        }

        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new BadRequestException("First name cannot be empty");
        }

        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new BadRequestException("Last name cannot be empty");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email ID cannot be empty");
        }

        String email = request.getEmail().trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Invalid email format (e.g., name@gmail.com)");
        }

        if (request.getContactNo() == null || request.getContactNo().trim().isEmpty()) {
            throw new BadRequestException("WhatsApp number cannot be empty");
        }

        String contactNo = request.getContactNo().trim();
        if (!WHATSAPP_PATTERN.matcher(contactNo).matches()) {
            throw new BadRequestException("WhatsApp number must be exactly 10 digits");
        }
    }
}
