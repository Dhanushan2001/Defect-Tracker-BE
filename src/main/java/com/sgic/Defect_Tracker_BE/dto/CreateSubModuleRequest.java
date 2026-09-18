package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubModuleRequest {
    private String name;
    private String subModuleName;
    private String description;
    private Long moduleId;
    private Long projectId;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (subModuleName != null && !subModuleName.trim().isEmpty()) {
            return subModuleName.trim();
        }
        return null;
    }
}
