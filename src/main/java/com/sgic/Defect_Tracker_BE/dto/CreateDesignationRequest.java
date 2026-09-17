package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDesignationRequest {
    private String name;
    private String designationName;
    private String description;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (designationName != null && !designationName.trim().isEmpty()) {
            return designationName.trim();
        }
        return null;
    }
}
