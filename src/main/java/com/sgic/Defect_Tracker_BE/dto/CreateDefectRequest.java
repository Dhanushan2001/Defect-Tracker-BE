package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDefectRequest {

    private String title;
    private String description;
    private String steps;

    @JsonProperty("stepsToRecreation")
    private String stepsToRecreationAlias;

    public String getSteps() {
        return steps != null ? steps : stepsToRecreationAlias;
    }

    private String attachment;

    private Long projectId;

    @JsonProperty("project_id")
    private Long projectIdAlias;

    private Long moduleId;

    @JsonProperty("modulesId")
    private Long modulesIdAlias;

    private Long subModuleId;

    @JsonProperty("submoduleId")
    private Long submoduleIdAlias;

    private Long releaseId;

    @JsonProperty("releasesId")
    private Long releasesIdAlias;

    private Long testCaseId;

    @JsonProperty("testcaseId")
    private Long testcaseIdAlias;

    private Long assignedTo;

    @JsonProperty("assignedToId")
    private Long assignedToIdAlias;

    @JsonProperty("assigntoId")
    private Long assigntoIdAlias;

    private Long reportedBy;

    @JsonProperty("reportedById")
    private Long reportedByIdAlias;

    @JsonProperty("assignbyId")
    private Long assignbyIdAlias;

    private Long severityId;
    private String severity;

    private Long priorityId;
    private String priority;

    private Long defectTypeId;

    @JsonProperty("typeId")
    private Long typeIdAlias;

    private String type;

    private Long statusId;

    @JsonProperty("defectStatusId")
    private Long defectStatusIdAlias;

    private String status;

    public Long resolveProjectId() {
        return projectId != null ? projectId : projectIdAlias;
    }

    public Long resolveModuleId() {
        return moduleId != null ? moduleId : modulesIdAlias;
    }

    public Long resolveSubModuleId() {
        return subModuleId != null ? subModuleId : submoduleIdAlias;
    }

    public Long resolveReleaseId() {
        return releaseId != null ? releaseId : releasesIdAlias;
    }

    public Long resolveTestCaseId() {
        return testCaseId != null ? testCaseId : testcaseIdAlias;
    }

    public Long resolveAssignedTo() {
        if (assignedTo != null && assignedTo > 0) return assignedTo;
        if (assignedToIdAlias != null && assignedToIdAlias > 0) return assignedToIdAlias;
        if (assigntoIdAlias != null && assigntoIdAlias > 0) return assigntoIdAlias;
        return null;
    }

    public Long resolveReportedBy() {
        if (reportedBy != null && reportedBy > 0) return reportedBy;
        if (reportedByIdAlias != null && reportedByIdAlias > 0) return reportedByIdAlias;
        if (assignbyIdAlias != null && assignbyIdAlias > 0) return assignbyIdAlias;
        return null;
    }

    public Long resolveDefectTypeId() {
        return defectTypeId != null ? defectTypeId : typeIdAlias;
    }

    public Long resolveStatusId() {
        return statusId != null ? statusId : defectStatusIdAlias;
    }
}
