package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCasePayloadDto {
    private String description;
    private String detailsSteps;
    private String steps;
    private String expectedResult;
    private Long subModuleId;
    private Long moduleId;
    private Long projectId;
    private Long severityId;
    private Long defectTypeId;
    private String testcaseNo;
    private String executionStatus;

    public String resolveSteps() {
        if (detailsSteps != null && !detailsSteps.isBlank()) {
            return detailsSteps;
        }
        return steps != null ? steps : "";
    }
}
