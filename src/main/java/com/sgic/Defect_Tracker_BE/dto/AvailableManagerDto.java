package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableManagerDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private Long designationId;
    private String designationName;
    private Integer availabilityPercent;
    private Boolean isActive;
}
