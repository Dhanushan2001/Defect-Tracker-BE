package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDefectTypeRequest {
    private String name;
    private String defectTypeName;
    private String description;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (defectTypeName != null && !defectTypeName.trim().isEmpty()) {
            return defectTypeName.trim();
        }
        return null;
    }
}
