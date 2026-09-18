package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAllocationHistoryDto {
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
    private Integer allocationPercent;
    private Integer allocationPercentage;
    private Integer percentage;
    private String startDate;
    private String endDate;
    private String action;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
}
