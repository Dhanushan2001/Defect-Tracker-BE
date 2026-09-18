package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationHistoryDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocationHistory;
import com.sgic.Defect_Tracker_BE.entity.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectAllocationHistoryMapper {

    public ProjectAllocationHistoryDto toDto(ProjectAllocationHistory entity) {
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

        String startDateStr = entity.getStartDate() != null ? entity.getStartDate().toString() : null;
        String endDateStr = entity.getEndDate() != null ? entity.getEndDate().toString() : null;
        String createdAtStr = entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null;
        String updatedAtStr = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null;

        return ProjectAllocationHistoryDto.builder()
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
                .allocationPercent(entity.getAllocationPercentage())
                .allocationPercentage(entity.getAllocationPercentage())
                .percentage(entity.getAllocationPercentage())
                .startDate(startDateStr)
                .endDate(endDateStr)
                .action(entity.getAction())
                .status(entity.getStatus())
                .createdAt(createdAtStr)
                .updatedAt(updatedAtStr)
                .build();
    }

    public List<ProjectAllocationHistoryDto> toDtoList(List<ProjectAllocationHistory> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProjectAllocationHistory fromAllocation(ProjectAllocation allocation, String action, Boolean status) {
        if (allocation == null) {
            return null;
        }
        boolean isDealloc = "DEALLOCATED".equalsIgnoreCase(action);
        LocalDate endDate = allocation.getEndDate();
        if (isDealloc) {
            endDate = LocalDate.now();
        }
        Boolean resolvedStatus = status != null ? status : !isDealloc;
        String resolvedAction = action != null ? action : (Boolean.FALSE.equals(resolvedStatus) ? "DEALLOCATED" : "ALLOCATED");

        return ProjectAllocationHistory.builder()
                .project(allocation.getProject())
                .employee(allocation.getEmployee())
                .role(allocation.getRole())
                .allocationPercentage(allocation.getAllocationPercentage() != null ? allocation.getAllocationPercentage() : 0)
                .startDate(allocation.getStartDate())
                .endDate(endDate)
                .action(resolvedAction)
                .status(resolvedStatus)
                .build();
    }
}
