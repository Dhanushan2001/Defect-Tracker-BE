package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePriorityRequest {
    private String name;
    private String priorityName;
    private String color;
    private Integer weight;
    private String description;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (priorityName != null && !priorityName.trim().isEmpty()) {
            return priorityName.trim();
        }
        return null;
    }
}
