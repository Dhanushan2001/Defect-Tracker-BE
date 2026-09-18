package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllocateSubModuleDeveloperRequest {
    private Long projectId;
    private Long moduleId;
    private Long subModuleId;
    private Long employeeId;
    private Long userId;

    public Long resolveEmployeeId() {
        return employeeId != null ? employeeId : userId;
    }
}
