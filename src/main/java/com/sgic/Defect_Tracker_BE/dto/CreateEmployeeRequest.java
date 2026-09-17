package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String contactNo;
    private Long designationId;
    private String joinDate;
    private Object isActive;
    private Object skills;
    private Integer experience;
    private Integer availability;

    public Boolean resolveIsActive() {
        if (isActive == null) {
            return true;
        }
        if (isActive instanceof Boolean) {
            return (Boolean) isActive;
        }
        String s = isActive.toString().trim();
        return "active".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "1".equals(s);
    }
}
