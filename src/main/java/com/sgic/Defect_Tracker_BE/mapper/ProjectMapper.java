package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateProjectRequest;
import com.sgic.Defect_Tracker_BE.dto.ProjectDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public ProjectDto toDto(Project entity) {
        if (entity == null) {
            return null;
        }

        Employee pm = entity.getProjectManager();
        Long pmId = pm != null ? pm.getId() : null;
        String pmName = pm != null ? ((pm.getFirstName() != null ? pm.getFirstName() : "") + " " +
                (pm.getLastName() != null ? pm.getLastName() : "")).trim() : null;
        Long pmDesignationId = (pm != null && pm.getDesignation() != null) ? pm.getDesignation().getId() : null;
        String pmDesignationName = (pm != null && pm.getDesignation() != null) ? pm.getDesignation().getName() : null;

        String startDateStr = entity.getStartDate() != null ? entity.getStartDate().toString() : null;
        String endDateStr = entity.getEndDate() != null ? entity.getEndDate().toString() : null;

        return ProjectDto.builder()
                .id(entity.getId())
                .projectId(entity.getId())
                .name(entity.getName())
                .projectName(entity.getName())
                .prefix(entity.getPrefix())
                .projectType(entity.getProjectType())
                .status(entity.getStatus() != null ? entity.getStatus() : "Active")
                .projectStatus(entity.getStatus() != null ? entity.getStatus() : "Active")
                .description(entity.getDescription())
                .startDate(startDateStr)
                .endDate(endDateStr)
                .projectManagerId(pmId)
                .projectManagerName(pmName)
                .projectManagerDesignationId(pmDesignationId)
                .projectManagerDesignationName(pmDesignationName)
                .managerAllocation(entity.getManagerAllocation() != null ? entity.getManagerAllocation() : 100)
                .clientName(entity.getClientName())
                .clientCountry(entity.getClientCountry())
                .country(entity.getClientCountry())
                .clientState(entity.getClientState())
                .state(entity.getClientState())
                .clientEmail(entity.getClientEmail())
                .email(entity.getClientEmail())
                .clientPhone(entity.getClientPhone())
                .phoneNo(entity.getClientPhone())
                .address(entity.getAddress())
                .build();
    }

    public List<ProjectDto> toDtoList(List<Project> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Project toEntity(CreateProjectRequest request, Employee projectManager) {
        if (request == null) {
            return null;
        }

        String status = (request.getStatus() != null && !request.getStatus().trim().isEmpty())
                ? request.getStatus().trim()
                : "Active";

        String prefix = request.getPrefix();
        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = generatePrefix(request.getName());
        }

        Integer allocation = request.getManagerAllocation() != null ? request.getManagerAllocation() : 100;

        return Project.builder()
                .name(request.getName() != null ? request.getName().trim() : null)
                .prefix(prefix.trim())
                .projectType(request.getProjectType() != null ? request.getProjectType().trim() : null)
                .status(status)
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .startDate(parseLocalDate(request.getStartDate()))
                .endDate(parseLocalDate(request.getEndDate()))
                .projectManager(projectManager)
                .managerAllocation(allocation)
                .clientName(request.getClientName() != null ? request.getClientName().trim() : null)
                .clientCountry(request.getClientCountry() != null ? request.getClientCountry().trim() : null)
                .clientState(request.getClientState() != null ? request.getClientState().trim() : null)
                .clientEmail(request.getClientEmail() != null ? request.getClientEmail().trim().toLowerCase() : null)
                .clientPhone(request.getClientPhone() != null ? request.getClientPhone().trim() : null)
                .address(request.getAddress() != null ? request.getAddress().trim() : null)
                .build();
    }

    public void updateEntity(Project entity, CreateProjectRequest request, Employee projectManager) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName().trim());
        }
        if (request.getPrefix() != null) {
            entity.setPrefix(request.getPrefix().trim());
        }
        if (request.getProjectType() != null) {
            entity.setProjectType(request.getProjectType().trim());
        }
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            entity.setStatus(request.getStatus().trim());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription().trim());
        }
        if (request.getStartDate() != null) {
            entity.setStartDate(parseLocalDate(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            entity.setEndDate(parseLocalDate(request.getEndDate()));
        }
        if (projectManager != null) {
            entity.setProjectManager(projectManager);
        }
        if (request.getManagerAllocation() != null) {
            entity.setManagerAllocation(request.getManagerAllocation());
        }
        if (request.getClientName() != null) {
            entity.setClientName(request.getClientName().trim());
        }
        if (request.getClientCountry() != null) {
            entity.setClientCountry(request.getClientCountry().trim());
        }
        if (request.getClientState() != null) {
            entity.setClientState(request.getClientState().trim());
        }
        if (request.getClientEmail() != null) {
            entity.setClientEmail(request.getClientEmail().trim().toLowerCase());
        }
        if (request.getClientPhone() != null) {
            entity.setClientPhone(request.getClientPhone().trim());
        }
        if (request.getAddress() != null) {
            entity.setAddress(request.getAddress().trim());
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

    private String generatePrefix(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "PROJ";
        }
        String[] words = name.trim().split("\\s+");
        if (words.length == 1) {
            return words[0].substring(0, Math.min(4, words[0].length())).toUpperCase();
        }
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0)));
            }
        }
        return sb.toString();
    }
}
