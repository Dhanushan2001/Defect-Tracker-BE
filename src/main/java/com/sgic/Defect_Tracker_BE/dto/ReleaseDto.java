package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseDto {

    private Long id;

    private String releaseId;

    private String name;

    private String releaseName;

    private String version;

    private String description;

    private String releaseDate;

    private String status;

    private Long projectId;

    @JsonProperty("project_id")
    private Long projectIdAlias;

    private String projectName;

    private Long releaseTypeId;

    @JsonProperty("releaseType_id")
    private Long releaseTypeIdAlias;

    private String releaseTypeName;

    @JsonProperty("releaseType_name")
    private String releaseTypeNameAlias;

    private ReleaseTypeDto releaseType;

    private Integer testCaseCount;

    @JsonProperty("test_case_count")
    private Integer testCaseCountAlias;

    private String createdAt;

    private String updatedAt;
}
