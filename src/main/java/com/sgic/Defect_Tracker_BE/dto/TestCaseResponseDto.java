package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResponseDto {
    private Long id;
    private String no;
    private String testcaseNo;
    private Long testCaseId;
    private String description;
    private String detailsSteps;
    private String steps;
    private String expectedResult;
    private Long subModuleId;
    private String subModuleName;
    private String subModule;
    private Long moduleId;
    private String moduleName;
    private String module;
    private Long projectId;
    private String projectName;
    private Long severityId;
    private String severityName;
    private String severity;
    private Long defectTypeId;
    private String defectTypeName;
    private String type;
    private String executionStatus;
    private String createdAt;
    private String updatedAt;
}
