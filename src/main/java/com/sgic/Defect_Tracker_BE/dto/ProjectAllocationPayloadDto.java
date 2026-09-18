package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAllocationPayloadDto {
    private Long employeeId;
    private Long projectId;
    private Long roleId;
    private Integer allocationPercent;
    private Integer allocationPercentage;
    private String startDate;
    private String endDate;

    public Integer resolveAllocationPercent() {
        if (allocationPercent != null) {
            return allocationPercent;
        }
        if (allocationPercentage != null) {
            return allocationPercentage;
        }
        return 0;
    }
}
