package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibleLeaderDto {
    private Long userId;
    private Long employeeId;
    private String userFullName;
    private String firstName;
    private String lastName;
    private String email;
    private Long roleId;
    private String roleName;
    private Long projectAllocationId;
    private Long projectId;
    private String userWithRole;
}
