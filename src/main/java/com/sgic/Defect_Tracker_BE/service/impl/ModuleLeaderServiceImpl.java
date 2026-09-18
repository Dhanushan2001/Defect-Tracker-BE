package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.AllocateModuleLeaderRequest;
import com.sgic.Defect_Tracker_BE.dto.EligibleLeaderDto;
import com.sgic.Defect_Tracker_BE.dto.ModuleLeaderResponseDto;
import com.sgic.Defect_Tracker_BE.entity.*;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.repository.ModuleLeaderRepository;
import com.sgic.Defect_Tracker_BE.repository.ModuleRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectAllocationRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectRepository;
import com.sgic.Defect_Tracker_BE.service.ModuleLeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ModuleLeaderServiceImpl implements ModuleLeaderService {

    private final ModuleLeaderRepository moduleLeaderRepository;
    private final ProjectAllocationRepository projectAllocationRepository;
    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;

    private boolean isQaLeadOrQaEngineer(Role role) {
        if (role == null) {
            return false;
        }
        String name = role.getRoleName() != null ? role.getRoleName().trim().toLowerCase().replaceAll("[_\\s]+", "") : "";
        String type = role.getRoleType() != null ? role.getRoleType().trim().toLowerCase().replaceAll("[_\\s]+", "") : "";
        return name.contains("qalead") || name.contains("qaengineer") || name.equals("qa") ||
               type.contains("qalead") || type.contains("qaengineer") || type.equals("qa");
    }

    @Override
    @Transactional(readOnly = true)
    public List<EligibleLeaderDto> getEligibleLeaders(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }

        List<ProjectAllocation> allocations = projectAllocationRepository.findByProjectIdAndIsActiveTrue(projectId);
        List<EligibleLeaderDto> eligibleList = new ArrayList<>();

        for (ProjectAllocation alloc : allocations) {
            if (alloc.getEmployee() != null && isQaLeadOrQaEngineer(alloc.getRole())) {
                Employee emp = alloc.getEmployee();
                Role role = alloc.getRole();

                String fName = emp.getFirstName() != null ? emp.getFirstName() : "";
                String lName = emp.getLastName() != null ? emp.getLastName() : "";
                String fullName = (fName + " " + lName).trim();
                String roleName = role != null && role.getRoleName() != null ? role.getRoleName() : "QA";

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
    public ModuleLeaderResponseDto allocateModuleLeader(AllocateModuleLeaderRequest request) {
        if (request == null || request.getProjectId() == null || request.getModuleId() == null || request.getUserId() == null) {
            throw new BadRequestException("projectId, moduleId, and userId are required to allocate module leader");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));

        if (module.getProject() == null || !module.getProject().getId().equals(project.getId())) {
            throw new BadRequestException("Module does not belong to project id: " + project.getId());
        }

        ProjectAllocation allocation = projectAllocationRepository
                .findByProjectIdAndEmployeeIdAndIsActiveTrue(project.getId(), request.getUserId())
                .orElseThrow(() -> new BadRequestException("Employee is not allocated to project id: " + project.getId()));

        if (!isQaLeadOrQaEngineer(allocation.getRole())) {
            throw new BadRequestException("Employee must be allocated to this project as QA Lead or QA Engineer to be assigned as Module Leader");
        }

        Employee employee = allocation.getEmployee();
        Role role = allocation.getRole();

        // Deactivate any existing active leader for this module
        moduleLeaderRepository.findByModuleIdAndIsActiveTrue(module.getId()).ifPresent(existing -> {
            existing.setIsActive(false);
            moduleLeaderRepository.save(existing);
        });

        // Create new ModuleLeader record
        ModuleLeader leader = ModuleLeader.builder()
                .project(project)
                .module(module)
                .employee(employee)
                .role(role)
                .assignedDate(LocalDate.now())
                .isActive(true)
                .build();

        ModuleLeader savedLeader = moduleLeaderRepository.save(leader);

        // Update module entity leader reference
        module.setLeader(employee);
        moduleRepository.save(module);

        log.info("Assigned employee {} ({}) as leader for module {} in project {}", 
                employee.getId(), role != null ? role.getRoleName() : "N/A", module.getId(), project.getId());

        return toDto(savedLeader);
    }

    @Override
    public void deallocateModuleLeader(Long allocateModuleId) {
        if (allocateModuleId == null) {
            throw new BadRequestException("allocateModuleId is required");
        }

        ModuleLeader ml = moduleLeaderRepository.findById(allocateModuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module leader allocation not found with id: " + allocateModuleId));

        ml.setIsActive(false);
        moduleLeaderRepository.save(ml);

        Module module = ml.getModule();
        if (module != null && module.getLeader() != null && ml.getEmployee() != null &&
                module.getLeader().getId().equals(ml.getEmployee().getId())) {
            module.setLeader(null);
            moduleRepository.save(module);
        }

        log.info("Deallocated module leader record id: {}", allocateModuleId);
    }

    @Override
    public void deallocateModuleLeaderByModuleId(Long moduleId) {
        if (moduleId == null) {
            throw new BadRequestException("moduleId is required");
        }

        moduleLeaderRepository.findByModuleIdAndIsActiveTrue(moduleId).ifPresent(ml -> {
            ml.setIsActive(false);
            moduleLeaderRepository.save(ml);
        });

        moduleRepository.findById(moduleId).ifPresent(module -> {
            module.setLeader(null);
            moduleRepository.save(module);
        });

        log.info("Deallocated module leader for module id: {}", moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleLeaderResponseDto getActiveLeaderByModuleId(Long moduleId) {
        if (moduleId == null) {
            return null;
        }

        return moduleLeaderRepository.findByModuleIdAndIsActiveTrue(moduleId)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleLeaderResponseDto> getActiveLeadersByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }

        return moduleLeaderRepository.findByProjectIdAndIsActiveTrue(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ModuleLeaderResponseDto toDto(ModuleLeader leader) {
        if (leader == null) {
            return null;
        }

        Employee emp = leader.getEmployee();
        String fName = emp != null && emp.getFirstName() != null ? emp.getFirstName() : "";
        String lName = emp != null && emp.getLastName() != null ? emp.getLastName() : "";
        String fullName = (fName + " " + lName).trim();

        Role role = leader.getRole();
        String roleName = role != null && role.getRoleName() != null ? role.getRoleName() : null;

        return ModuleLeaderResponseDto.builder()
                .allocateModuleId(leader.getId())
                .moduleId(leader.getModule() != null ? leader.getModule().getId() : null)
                .moduleName(leader.getModule() != null ? leader.getModule().getName() : null)
                .projectId(leader.getProject() != null ? leader.getProject().getId() : null)
                .projectName(leader.getProject() != null ? leader.getProject().getName() : null)
                .userId(emp != null ? emp.getId() : null)
                .employeeId(emp != null ? emp.getId() : null)
                .userName(fullName)
                .employeeName(fullName)
                .roleId(role != null ? role.getId() : null)
                .roleName(roleName)
                .assignedDate(leader.getAssignedDate() != null ? leader.getAssignedDate().toString() : null)
                .isActive(leader.getIsActive())
                .build();
    }
}
