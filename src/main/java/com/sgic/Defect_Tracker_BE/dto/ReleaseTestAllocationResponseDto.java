package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReleaseTestAllocationResponseDto {

    private Long id;                  // allocation ID
    private Long testcaseId;          // numeric test case ID
    private String testCaseId;        // formatted test case code (e.g. TC-001)
    private String testCaseNo;        // formatted test case code (e.g. TC-001)
    private String name;              // test case name / description
    private String description;       // test case description
    private String steps;             // execution steps
    private String detailsSteps;      // detailed steps
    private String expectedResult;    // expected result
    private String type;              // defect type name
    private String defectTypeName;    // defect type name
    private String severity;          // severity name
    private String severityName;      // severity name
    private Long projectId;
    private String projectName;
    private Long moduleId;
    private String moduleName;
    private Long submoduleId;
    private Long subModuleId;
    private String subModuleName;
    private Long releaseId;
    private String releaseName;
    private Long assignedTo;          // QA employee ID
    private Long assignedQaId;        // QA employee ID
    private String assignedQaName;    // QA employee name
    private String status;            // allocation status
    private String executionStatus;   // test execution status (PASS, FAIL, NOT_RUN)
    private String defectNo;          // generated defect number (DEF-xxx)
    private Long defectId;            // defect entity id
    private Long assignedDevId;       // assigned developer id
    private String assignedDev;       // assigned developer name
    private String assignedDevName;   // assigned developer name
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
