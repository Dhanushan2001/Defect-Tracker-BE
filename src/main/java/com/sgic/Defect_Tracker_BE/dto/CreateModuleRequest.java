package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuleRequest {
    private String name;
    private String moduleName;
    private String description;
    private Long projectId;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (moduleName != null && !moduleName.trim().isEmpty()) {
            return moduleName.trim();
        }
        return null;
    }
}
