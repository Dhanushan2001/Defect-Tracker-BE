package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStatusTypeRequest {
    private String name;
    private String statusName;
    private String color;
    private String type;
    private String statusType;
    private String description;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (statusName != null && !statusName.trim().isEmpty()) {
            return statusName.trim();
        }
        return null;
    }

    public String resolveType() {
        if (type != null && !type.trim().isEmpty()) {
            return type.trim();
        }
        if (statusType != null && !statusType.trim().isEmpty()) {
            return statusType.trim();
        }
        return null;
    }
}
