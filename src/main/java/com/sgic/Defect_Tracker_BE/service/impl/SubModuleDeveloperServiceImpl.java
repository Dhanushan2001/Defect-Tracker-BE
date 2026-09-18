package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.AllocateSubModuleDeveloperRequest;
import com.sgic.Defect_Tracker_BE.dto.EligibleLeaderDto;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDeveloperResponseDto;
import com.sgic.Defect_Tracker_BE.entity.*;
import com.sgic.Defect_Tracker_BE.repository.ProjectAllocationRepository;
import com.sgic.Defect_Tracker_BE.repository.SubModuleDeveloperRepository;
import com.sgic.Defect_Tracker_BE.repository.SubModuleRepository;
import com.sgic.Defect_Tracker_BE.service.SubModuleDeveloperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubModuleDeveloperServiceImpl implements SubModuleDeveloperService {

    private final SubModuleDeveloperRepository subModuleDeveloperRepository;
    private final SubModuleRepository subModuleRepository;
    private final ProjectAllocationRepository projectAllocationRepository;

    private boolean isDeveloperRole(Role role) {
        if (role == null) {
            return false;
        }
        String name = role.getRoleName() != null ? role.getRoleName().trim().toLowerCase().replaceAll("[_\\s]+", "") : "";
        String type = role.getRoleType() != null ? role.getRoleType().trim().toLowerCase().replaceAll("[_\\s]+", "") : "";

        // Strictly exclude QA and Project Managers
        if (name.contains("qa") || type.contains("qa") || name.contains("manager") || type.contains("manager")) {
            return false;
        }

        return name.contains("developer") || name.contains("devlead") || name.contains("dev") ||
               type.contains("developer") || type.contains("devlead") || type.contains("dev");
    }

    @Override
    @Transactional(readOnly = true)
    public List<EligibleLeaderDto> getEligibleDevelopers(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }

        List<ProjectAllocation> allocations = projectAllocationRepository.findByProjectIdAndIsActiveTrue(projectId);
        List<EligibleLeaderDto> eligibleList = new ArrayList<>();

        for (ProjectAllocation alloc : allocations) {
            if (alloc.getEmployee() != null && isDeveloperRole(alloc.getRole())) {
                Employee emp = alloc.getEmployee();
                Role role = alloc.getRole();

                String fName = emp.getFirstName() != null ? emp.getFirstName() : "";
                String lName = emp.getLastName() != null ? emp.getLastName() : "";
                String fullName = (fName + " " + lName).trim();
                String roleName = role != null && role.getRoleName() != null ? role.getRoleName() : "Developer";

                EligibleLeaderDto dto = EligibleLeaderDto.builder()
                        .userId(emp.getId())
                        .employeeId(emp.getId())
                        .userFullName(fullName)
                        .firstName(fName)
                        .lastName(lName)
                        .email(emp.getEmail())
                        .roleId(role != null ? role.getId() : null)
                        .roleName(roleName)
                        .projectAllocationId(alloc.getId())
                        .projectId(projectId)
                        .userWithRole(fullName + " (" + roleName + ")")
                        .build();

                eligibleList.add(dto);
            }
        }

        return eligibleList;
    }

    @Override
    public SubModuleDeveloperResponseDto allocateDeveloper(AllocateSubModuleDeveloperRequest request) {
        if (request == null || request.getSubModuleId() == null || request.resolveEmployeeId() == null) {
            throw new BadRequestException("subModuleId and employeeId (or userId) are required to allocate developer");
        }
        return allocateDeveloper(request.getSubModuleId(), request.resolveEmployeeId());
    }

    @Override
    public SubModuleDeveloperResponseDto allocateDeveloper(Long subModuleId, Long employeeId) {
        if (subModuleId == null || employeeId == null) {
            throw new BadRequestException("subModuleId and employeeId are required");
        }

        SubModule subModule = subModuleRepository.findById(subModuleId)
                .orElseThrow(() -> new ResourceNotFoundException("SubModule not found with id: " + subModuleId));

        Project project = subModule.getProject();
        if (project == null) {
            throw new BadRequestException("SubModule is not associated with a project");
        }

        ProjectAllocation allocation = projectAllocationRepository
                .findByProjectIdAndEmployeeIdAndIsActiveTrue(project.getId(), employeeId)
                .orElseThrow(() -> new BadRequestException("Employee is not allocated to project id: " + project.getId()));

        if (!isDeveloperRole(allocation.getRole())) {
            throw new BadRequestException("Employee must be allocated to this project as a Dev Lead, Developer, Senior Developer, or Junior Developer");
        }

        // Check if already actively allocated to this submodule
        Optional<SubModuleDeveloper> existing = subModuleDeveloperRepository
                .findBySubModuleIdAndEmployeeIdAndIsActiveTrue(subModuleId, employeeId);
        if (existing.isPresent()) {
            return toDto(existing.get());
        }

        SubModuleDeveloper developer = SubModuleDeveloper.builder()
                .project(project)
                .module(subModule.getModule())
                .subModule(subModule)
                .employee(allocation.getEmployee())
                .role(allocation.getRole())
                .assignedDate(LocalDate.now())
                .isActive(true)
                .build();

        SubModuleDeveloper saved = subModuleDeveloperRepository.save(developer);

        log.info("Allocated employee {} ({}) to submodule {} in project {}",
                employeeId, allocation.getRole() != null ? allocation.getRole().getRoleName() : "Developer",
                subModuleId, project.getId());

        return toDto(saved);
    }

    @Override
    public void deallocateDeveloper(Long subModuleId, Long employeeId) {
        if (subModuleId == null || employeeId == null) {
            throw new BadRequestException("subModuleId and employeeId are required");
        }

        subModuleDeveloperRepository.findBySubModuleIdAndEmployeeIdAndIsActiveTrue(subModuleId, employeeId)
                .ifPresent(record -> {
                    record.setIsActive(false);
                    subModuleDeveloperRepository.save(record);
                    log.info("Deallocated employee {} from submodule {}", employeeId, subModuleId);
                });
    }

    @Override
    public void deallocateById(Long id) {
        if (id == null) {
            throw new BadRequestException("id is required");
        }

        subModuleDeveloperRepository.findById(id).ifPresent(record -> {
            record.setIsActive(false);
            subModuleDeveloperRepository.save(record);
            log.info("Deallocated submodule developer allocation record id {}", id);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubModuleDeveloperResponseDto> getDevelopersBySubModuleId(Long subModuleId) {
        if (subModuleId == null) {
            return Collections.emptyList();
        }

        return subModuleDeveloperRepository.findBySubModuleIdAndIsActiveTrue(subModuleId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubModuleDeveloperResponseDto> getDevelopersByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }

        return subModuleDeveloperRepository.findByProjectIdAndIsActiveTrue(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SubModuleDeveloperResponseDto toDto(SubModuleDeveloper record) {
        if (record == null) {
            return null;
        }

        Employee emp = record.getEmployee();
        String fName = emp != null && emp.getFirstName() != null ? emp.getFirstName() : "";
        String lName = emp != null && emp.getLastName() != null ? emp.getLastName() : "";
        String fullName = (fName + " " + lName).trim();

        Role role = record.getRole();
        String roleName = role != null && role.getRoleName() != null ? role.getRoleName() : "Developer";

        return SubModuleDeveloperResponseDto.builder()
                .id(record.getId())
                .subModuleId(record.getSubModule() != null ? record.getSubModule().getId() : null)
                .subModuleName(record.getSubModule() != null ? record.getSubModule().getName() : null)
                .moduleId(record.getModule() != null ? record.getModule().getId() : null)
                .moduleName(record.getModule() != null ? record.getModule().getName() : null)
                .projectId(record.getProject() != null ? record.getProject().getId() : null)
                .projectName(record.getProject() != null ? record.getProject().getName() : null)
                .employeeId(emp != null ? emp.getId() : null)
                .userId(emp != null ? emp.getId() : null)
                .employeeName(fullName)
                .userName(fullName)
                .firstName(fName)
                .lastName(lName)
                .email(emp != null ? emp.getEmail() : null)
                .roleId(role != null ? role.getId() : null)
                .roleName(roleName)
                .assignedDate(record.getAssignedDate() != null ? record.getAssignedDate().toString() : null)
                .isActive(record.getIsActive())
                .build();
    }
}
