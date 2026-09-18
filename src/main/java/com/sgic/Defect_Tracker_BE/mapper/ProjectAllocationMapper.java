package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationPayloadDto;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationResponseDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import com.sgic.Defect_Tracker_BE.entity.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectAllocationMapper {

    public ProjectAllocationResponseDto toDto(ProjectAllocation entity) {
        if (entity == null) {
            return null;
        }

        Employee emp = entity.getEmployee();
        Project proj = entity.getProject();
        Role role = entity.getRole();

        String firstName = emp != null && emp.getFirstName() != null ? emp.getFirstName() : "";
        String lastName = emp != null && emp.getLastName() != null ? emp.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();

        Long roleId = role != null ? role.getId() : null;
        String roleName = role != null ? role.getName() : "Developer";

        Long designationId = (emp != null && emp.getDesignation() != null) ? emp.getDesignation().getId() : null;
        String designationName = (emp != null && emp.getDesignation() != null) ? emp.getDesignation().getName() : "";

        String startDateStr = entity.getStartDate() != null ? entity.getStartDate().toString() : null;
        String endDateStr = entity.getEndDate() != null ? entity.getEndDate().toString() : null;

        return ProjectAllocationResponseDto.builder()
                .id(entity.getId())
                .projectId(proj != null ? proj.getId() : null)
                .projectName(proj != null ? proj.getName() : null)
                .employeeId(emp != null ? emp.getId() : null)
                .userId(emp != null ? emp.getId() : null)
                .firstName(firstName)
                .lastName(lastName)
                .userFullName(fullName)
                .employeeName(fullName)
                .email(emp != null ? emp.getEmail() : null)
                .contactNo(emp != null ? emp.getContactNo() : null)
                .roleId(roleId)
                .roleName(roleName)
                .designationId(designationId)
                .designationName(designationName)
                .allocationPercent(entity.getAllocationPercentage())
                .allocationPercentage(entity.getAllocationPercentage())
                .startDate(startDateStr)
                .endDate(endDateStr)
                .isActive(entity.getIsActive())
                .build();
    }

    public List<ProjectAllocationResponseDto> toDtoList(List<ProjectAllocation> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProjectAllocation toEntity(ProjectAllocationPayloadDto payload, Project project, Employee employee, Role role) {
        if (payload == null) {
            return null;
        }

        LocalDate startDate = parseLocalDate(payload.getStartDate());
        LocalDate endDate = parseLocalDate(payload.getEndDate());
        Integer percent = payload.resolveAllocationPercent();

        return ProjectAllocation.builder()
                .project(project)
                .employee(employee)
                .role(role)
                .allocationPercentage(percent)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(true)
                .build();
    }

    public void updateEntity(ProjectAllocation entity, ProjectAllocationPayloadDto payload, Role role) {
        if (entity == null || payload == null) {
            return;
        }

        if (role != null) {
            entity.setRole(role);
        }
        if (payload.getStartDate() != null) {
            entity.setStartDate(parseLocalDate(payload.getStartDate()));
        }
        if (payload.getEndDate() != null) {
            entity.setEndDate(parseLocalDate(payload.getEndDate()));
        }
        if (payload.resolveAllocationPercent() > 0) {
            entity.setAllocationPercentage(payload.resolveAllocationPercent());
        }
    }

    private LocalDate parseLocalDate(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(str.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            try {
                return LocalDate.parse(str.trim());
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
