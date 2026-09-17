package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.AvailableManagerDto;
import com.sgic.Defect_Tracker_BE.dto.CreateProjectRequest;
import com.sgic.Defect_Tracker_BE.dto.ProjectDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.mapper.ProjectMapper;
import com.sgic.Defect_Tracker_BE.repository.EmployeeRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectRepository;
import com.sgic.Defect_Tracker_BE.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_10_DIGIT_PATTERN =
            Pattern.compile("^\\d{10}$");

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectMapper.toDtoList(projectRepository.findAllByOrderByIdAsc());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto createProject(CreateProjectRequest request) {
        validateProjectRequest(request);

        String name = request.getName().trim();
        if (projectRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Project with name '" + name + "' already exists");
        }

        Employee manager = null;
        if (request.getProjectManagerId() != null) {
            manager = employeeRepository.findById(request.getProjectManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getProjectManagerId()));
        }

        Project project = projectMapper.toEntity(request, manager);
        Project saved = projectRepository.save(project);
        return projectMapper.toDto(saved);
    }

    @Override
    public ProjectDto updateProject(Long id, CreateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        validateProjectRequest(request);

        String name = request.getName().trim();
        if (projectRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Project with name '" + name + "' already exists");
        }

        Employee manager = project.getProjectManager();
        if (request.getProjectManagerId() != null) {
            manager = employeeRepository.findById(request.getProjectManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getProjectManagerId()));
        }

        projectMapper.updateEntity(project, request, manager);
        Project updated = projectRepository.save(project);
        return projectMapper.toDto(updated);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableManagerDto> getAvailableManagers(Long designationId) {
        if (designationId == null) {
            return Collections.emptyList();
        }

        List<Employee> employees = employeeRepository.findByDesignationId(designationId);
        return employees.stream()
                .filter(e -> e.getIsActive() != null && e.getIsActive())
                .sorted(Comparator.comparing(Employee::getId))
                .map(e -> AvailableManagerDto.builder()
                        .employeeId(e.getId())
                        .firstName(e.getFirstName())
                        .lastName(e.getLastName())
                        .email(e.getEmail())
                        .designationId(e.getDesignation() != null ? e.getDesignation().getId() : null)
                        .designationName(e.getDesignation() != null ? e.getDesignation().getName() : "")
                        .availabilityPercent(e.getAvailability() != null ? e.getAvailability() : 100)
                        .isActive(e.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }

    private void validateProjectRequest(CreateProjectRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Project name is required");
        }

        if (request.getProjectManagerId() == null) {
            throw new BadRequestException("Please select a project manager");
        }

        if (request.getClientEmail() != null && !request.getClientEmail().trim().isEmpty()) {
            String email = request.getClientEmail().trim();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new BadRequestException("Please enter a valid email address (e.g., example@gmail.com)");
            }
        }

        if (request.getClientPhone() != null && !request.getClientPhone().trim().isEmpty()) {
            String phone = request.getClientPhone().trim();
            if (!PHONE_10_DIGIT_PATTERN.matcher(phone).matches()) {
                throw new BadRequestException("Phone number must be exactly 10 digits");
            }
        }
    }
}
