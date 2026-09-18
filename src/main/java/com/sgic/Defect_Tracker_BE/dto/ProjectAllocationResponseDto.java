package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAllocationResponseDto {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long employeeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String userFullName;
    private String employeeName;
    private String email;
    private String contactNo;
    private Long roleId;
    private String roleName;
    private Long designationId;
    private String designationName;
    private Integer allocationPercent;
    private Integer allocationPercentage;
    private String startDate;
    private String endDate;
    private Boolean isActive;
}
