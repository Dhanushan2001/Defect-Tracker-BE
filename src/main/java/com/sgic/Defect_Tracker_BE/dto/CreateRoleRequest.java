package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {
    private String name;
    private String roleName;
    private String type;
    private String roleType;
    private String description;

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (roleName != null && !roleName.trim().isEmpty()) {
            return roleName.trim();
        }
        return null;
    }

    public String resolveType() {
        if (type != null && !type.trim().isEmpty()) {
            return type.trim();
        }
        if (roleType != null && !roleType.trim().isEmpty()) {
            return roleType.trim();
        }
        return null;
    }
}
