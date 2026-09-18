package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationPayloadDto;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationResponseDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import com.sgic.Defect_Tracker_BE.entity.Role;
import com.sgic.Defect_Tracker_BE.mapper.ProjectAllocationMapper;
import com.sgic.Defect_Tracker_BE.repository.EmployeeRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectAllocationRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectRepository;
import com.sgic.Defect_Tracker_BE.repository.RoleRepository;
import com.sgic.Defect_Tracker_BE.service.ProjectAllocationHistoryService;
import com.sgic.Defect_Tracker_BE.service.ProjectAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {

    private final ProjectAllocationRepository projectAllocationRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final ProjectAllocationMapper projectAllocationMapper;
    private final ProjectAllocationHistoryService projectAllocationHistoryService;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAllocationResponseDto> getAllAllocations() {
        return projectAllocationMapper.toDtoList(projectAllocationRepository.findByIsActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAllocationResponseDto> getAllocationsByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        return projectAllocationMapper.toDtoList(projectAllocationRepository.findByProjectIdAndIsActiveTrue(projectId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAllocationResponseDto> getAllocationsByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return Collections.emptyList();
        }
        return projectAllocationMapper.toDtoList(projectAllocationRepository.findByEmployeeIdAndIsActiveTrue(employeeId));
    }

    @Override
    public ProjectAllocationResponseDto allocate(ProjectAllocationPayloadDto payload) {
        if (payload == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        if (payload.getProjectId() == null) {
            throw new BadRequestException("Project ID is required");
        }
        if (payload.getEmployeeId() == null) {
            throw new BadRequestException("Employee ID is required");
        }

        int percent = payload.resolveAllocationPercent();
        if (percent <= 0) {
            throw new BadRequestException("Allocation percentage must be greater than 0");
        }

        Project project = projectRepository.findById(payload.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + payload.getProjectId()));

        Employee employee = employeeRepository.findById(payload.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + payload.getEmployeeId()));

        int currentAvail = employee.getAvailability() != null ? employee.getAvailability() : 100;
        if (percent > currentAvail) {
            throw new BadRequestException("Cannot allocate " + percent + "%. Employee only has " + currentAvail + "% availability remaining.");
        }

        Role role = null;
        if (payload.getRoleId() != null) {
            role = roleRepository.findById(payload.getRoleId()).orElse(null);
        }

        // Deduct allocated percentage from employee availability
        int remaining = Math.max(0, currentAvail - percent);
        employee.setAvailability(remaining);
        employeeRepository.save(employee);

        // Check if employee already has active allocation in this project
        Optional<ProjectAllocation> existingOpt = projectAllocationRepository
                .findByProjectIdAndEmployeeIdAndIsActiveTrue(project.getId(), employee.getId());

        ProjectAllocation saved;
        if (existingOpt.isPresent()) {
            ProjectAllocation existing = existingOpt.get();
            existing.setAllocationPercentage(existing.getAllocationPercentage() + percent);
            if (role != null) {
                existing.setRole(role);
            }
            if (payload.getStartDate() != null) {
                projectAllocationMapper.updateEntity(existing, payload, role);
            }
            saved = projectAllocationRepository.save(existing);
        } else {
            ProjectAllocation entity = projectAllocationMapper.toEntity(payload, project, employee, role);
            saved = projectAllocationRepository.save(entity);
        }

        projectAllocationHistoryService.recordFromAllocation(saved, "ALLOCATED", true);
        return projectAllocationMapper.toDto(saved);
    }

    @Override
    public ProjectAllocationResponseDto updateAllocation(Long id, ProjectAllocationPayloadDto payload) {
        ProjectAllocation allocation = projectAllocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with id: " + id));

        int oldPercent = allocation.getAllocationPercentage();
        int newPercent = payload.resolveAllocationPercent();

        if (newPercent > 0 && newPercent != oldPercent) {
            Employee employee = allocation.getEmployee();
            int currentAvail = employee.getAvailability() != null ? employee.getAvailability() : 0;
            int delta = newPercent - oldPercent;
            if (delta > 0 && delta > currentAvail) {
                throw new BadRequestException("Cannot increase allocation by " + delta + "%. Employee only has " + currentAvail + "% availability remaining.");
            }
            int updatedAvail = Math.max(0, Math.min(100, currentAvail - delta));
            employee.setAvailability(updatedAvail);
            employeeRepository.save(employee);
            allocation.setAllocationPercentage(newPercent);
        }

        Role role = null;
        if (payload.getRoleId() != null) {
            role = roleRepository.findById(payload.getRoleId()).orElse(null);
        }

        projectAllocationMapper.updateEntity(allocation, payload, role);
        ProjectAllocation saved = projectAllocationRepository.save(allocation);
        projectAllocationHistoryService.recordFromAllocation(saved, "UPDATED", true);
        return projectAllocationMapper.toDto(saved);
    }

    @Override
    public void deallocate(Long allocationId) {
        ProjectAllocation allocation = projectAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with id: " + allocationId));

        Employee employee = allocation.getEmployee();
        if (employee != null) {
            int currentAvail = employee.getAvailability() != null ? employee.getAvailability() : 0;
            int restored = Math.min(100, currentAvail + allocation.getAllocationPercentage());
            employee.setAvailability(restored);
            employeeRepository.save(employee);
        }

        projectAllocationHistoryService.recordFromAllocation(allocation, "DEALLOCATED", false);
        projectAllocationRepository.delete(allocation);
    }

    @Override
    public void deallocateByEmployeeId(Long employeeId) {
        List<ProjectAllocation> allocations = projectAllocationRepository.findByEmployeeIdAndIsActiveTrue(employeeId);
        for (ProjectAllocation alloc : allocations) {
            deallocate(alloc.getId());
        }
    }

    @Override
    public void deallocateByProjectAndEmployee(Long projectId, Long employeeId) {
        if (projectId == null || employeeId == null) {
            return;
        }
        Optional<ProjectAllocation> opt = projectAllocationRepository
                .findByProjectIdAndEmployeeIdAndIsActiveTrue(projectId, employeeId);
        if (opt.isPresent()) {
            deallocate(opt.get().getId());
        } else {
            // Also check by employee in project even if not active or find any matching allocation
            Optional<ProjectAllocation> anyOpt = projectAllocationRepository
                    .findByProjectIdAndEmployeeId(projectId, employeeId);
            anyOpt.ifPresent(alloc -> deallocate(alloc.getId()));
        }
    }
}
