package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleLeaderResponseDto {
    private Long allocateModuleId;
    private Long moduleId;
    private String moduleName;
    private Long projectId;
    private String projectName;
    private Long userId;
    private Long employeeId;
    private String userName;
    private String employeeName;
    private Long roleId;
    private String roleName;
    private String assignedDate;
    private Boolean isActive;
}
