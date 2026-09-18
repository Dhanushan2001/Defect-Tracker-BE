package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto {
    private Long id;
    private String name;
    private String moduleName;
    private String description;
    private Long projectId;
    private String projectName;

    @Builder.Default
    private List<SubModuleDto> submodules = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedDev {
        private Long userId;
        private String userName;
        private Long allocateModuleId;
        private String roleName;
    }

    private AssignedDev assignedDev;
    private Long leaderId;
    private String leaderName;

    private String createdAt;
    private String updatedAt;
}
