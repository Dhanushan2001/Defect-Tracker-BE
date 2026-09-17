package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReleaseTypeRequest {
    private String name;
    private String releaseTypeName;
    private String description;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (releaseTypeName != null && !releaseTypeName.trim().isEmpty()) {
            return releaseTypeName.trim();
        }
        return null;
    }
}
