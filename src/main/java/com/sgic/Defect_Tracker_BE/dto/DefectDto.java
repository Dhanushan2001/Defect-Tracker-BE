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
public class DefectDto {

    private Long id;
    private String defectNo;
    private String defectId;
    private String title;
    private String description;
    private String steps;
    private String attachment;

    private Long projectId;
    private String projectName;

    private Long moduleId;
    private String moduleName;

    private Long submoduleId;
    private Long subModuleId;
    private String subModuleName;

    private Long releaseId;
    private String releaseName;

    private Long testCaseId;
    private String testCaseNo;

    private Long assignedTo;
    private Long assignedToId;
    private String assignedToName;
    private String executerDefect;

    private Long reportedBy;
    private Long reportedById;
    private String reportedByName;

    private Long severityId;
    private String severityName;
    private String severity;

    private Long priorityId;
    private String priorityName;
    private String priority;

    private Long defectTypeId;
    private String defectTypeName;
    private String type;

    private Long statusId;
    private String statusName;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long commentCount;
    private Long commentsCount;
}
