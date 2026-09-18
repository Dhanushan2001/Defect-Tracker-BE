package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReleaseRequest {

    @JsonAlias({"releaseName"})
    private String name;

    private String releaseName;

    private String version;

    private String description;

    private String releaseDate;

    @JsonAlias({"releaseType_id", "release_type_id"})
    private Long releaseTypeId;

    @JsonProperty("releaseType_id")
    private Long releaseTypeIdAlias;

    @JsonAlias({"releaseType_name", "release_type_name"})
    private String releaseTypeName;

    @JsonAlias({"project_id"})
    private Long projectId;

    @JsonProperty("project_id")
    private Long projectIdAlias;

    private String status;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (releaseName != null && !releaseName.trim().isEmpty()) {
            return releaseName.trim();
        }
        return null;
    }

    public Long resolveProjectId() {
        if (projectId != null) {
            return projectId;
        }
        return projectIdAlias;
    }

    public Long resolveReleaseTypeId() {
        if (releaseTypeId != null) {
            return releaseTypeId;
        }
        return releaseTypeIdAlias;
    }

    public LocalDate resolveReleaseDate() {
        if (releaseDate == null || releaseDate.trim().isEmpty()) {
            return null;
        }
        try {
            String trimmed = releaseDate.trim();
            if (trimmed.contains("T")) {
                trimmed = trimmed.split("T")[0];
            }
            return LocalDate.parse(trimmed, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }
}
