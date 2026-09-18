package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubModuleDto {
    private Long id;
    private String name;
    private String subModuleName;
    private String description;
    private Long moduleId;
    private String moduleName;
    private Long projectId;
    private String projectName;
    private String createdAt;
    private String updatedAt;
}
