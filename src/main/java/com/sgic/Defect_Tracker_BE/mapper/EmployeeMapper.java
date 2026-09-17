package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateEmployeeRequest;
import com.sgic.Defect_Tracker_BE.dto.EmployeeDto;
import com.sgic.Defect_Tracker_BE.entity.Designation;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee entity) {
        if (entity == null) {
            return null;
        }

        String fullName = ((entity.getFirstName() != null ? entity.getFirstName() : "") + " " +
                (entity.getLastName() != null ? entity.getLastName() : "")).trim();

        List<String> skillsList = Collections.emptyList();
        if (entity.getSkills() != null && !entity.getSkills().trim().isEmpty()) {
            skillsList = Arrays.stream(entity.getSkills().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        String joinDateStr = entity.getJoinDate() != null ? entity.getJoinDate().toString() : null;

        Long designationId = entity.getDesignation() != null ? entity.getDesignation().getId() : null;
        String designationName = entity.getDesignation() != null ? entity.getDesignation().getName() : null;

        boolean active = entity.getIsActive() != null && entity.getIsActive();

        return EmployeeDto.builder()
                .id(entity.getId())
                .userId(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .name(fullName)
                .gender(entity.getGender())
                .email(entity.getEmail())
                .contactNo(entity.getContactNo())
                .designationId(designationId)
                .designationName(designationName)
                .joinDate(joinDateStr)
                .isActive(active)
                .status(active ? "active" : "inactive")
                .skills(skillsList)
                .experience(entity.getExperience())
                .availability(entity.getAvailability() != null ? entity.getAvailability() : 100)
                .build();
    }

    public List<EmployeeDto> toDtoList(List<Employee> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Employee toEntity(CreateEmployeeRequest request, Designation designation) {
        if (request == null) {
            return null;
        }

        LocalDate joinDate = parseLocalDate(request.getJoinDate());
        String skillsStr = formatSkills(request.getSkills());

        return Employee.builder()
                .firstName(formatName(request.getFirstName()))
                .lastName(formatName(request.getLastName()))
                .gender(request.getGender())
                .email(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null)
                .contactNo(request.getContactNo() != null ? request.getContactNo().trim() : null)
                .designation(designation)
                .joinDate(joinDate)
                .isActive(request.resolveIsActive())
                .skills(skillsStr)
                .experience(request.getExperience() != null ? request.getExperience() : 0)
                .availability(request.getAvailability() != null ? request.getAvailability() : 100)
                .build();
    }

    public void updateEntity(Employee entity, CreateEmployeeRequest request, Designation designation) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getFirstName() != null) {
            entity.setFirstName(formatName(request.getFirstName()));
        }
        if (request.getLastName() != null) {
            entity.setLastName(formatName(request.getLastName()));
        }
        if (request.getGender() != null) {
            entity.setGender(request.getGender());
        }
        if (request.getEmail() != null) {
            entity.setEmail(request.getEmail().trim().toLowerCase());
        }
        if (request.getContactNo() != null) {
            entity.setContactNo(request.getContactNo().trim());
        }
        if (designation != null) {
            entity.setDesignation(designation);
        }
        if (request.getJoinDate() != null) {
            entity.setJoinDate(parseLocalDate(request.getJoinDate()));
        }
        if (request.getIsActive() != null) {
            entity.setIsActive(request.resolveIsActive());
        }
        if (request.getSkills() != null) {
            entity.setSkills(formatSkills(request.getSkills()));
        }
        if (request.getExperience() != null) {
            entity.setExperience(request.getExperience());
        }
        if (request.getAvailability() != null) {
            entity.setAvailability(request.getAvailability());
        } else if (entity.getAvailability() == null) {
            entity.setAvailability(100);
        }
    }

    private String formatName(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        String trimmed = str.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return LocalDate.now();
        }
        try {
            String clean = dateStr.trim();
            if (clean.contains("T")) {
                clean = clean.split("T")[0];
            }
            return LocalDate.parse(clean, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    @SuppressWarnings("unchecked")
    private String formatSkills(Object skillsObj) {
        if (skillsObj == null) {
            return "";
        }
        if (skillsObj instanceof List) {
            List<?> list = (List<?>) skillsObj;
            return list.stream().map(Object::toString).collect(Collectors.joining(", "));
        }
        return skillsObj.toString();
    }
}
