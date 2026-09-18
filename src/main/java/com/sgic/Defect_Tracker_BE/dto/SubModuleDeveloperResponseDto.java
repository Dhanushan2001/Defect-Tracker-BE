package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubModuleDeveloperResponseDto {
    private Long id;
    private Long subModuleId;
    private String subModuleName;
    private Long moduleId;
    private String moduleName;
    private Long projectId;
    private String projectName;
    private Long employeeId;
    private Long userId;
    private String employeeName;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Long roleId;
    private String roleName;
    private String assignedDate;
    private Boolean isActive;
}
