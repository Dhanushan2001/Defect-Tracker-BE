package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.AllocateModuleLeaderRequest;
import com.sgic.Defect_Tracker_BE.dto.EligibleLeaderDto;
import com.sgic.Defect_Tracker_BE.dto.ModuleLeaderResponseDto;

import java.util.List;

public interface ModuleLeaderService {

    List<EligibleLeaderDto> getEligibleLeaders(Long projectId);

    ModuleLeaderResponseDto allocateModuleLeader(AllocateModuleLeaderRequest request);

    void deallocateModuleLeader(Long allocateModuleId);

    void deallocateModuleLeaderByModuleId(Long moduleId);

    ModuleLeaderResponseDto getActiveLeaderByModuleId(Long moduleId);

    List<ModuleLeaderResponseDto> getActiveLeadersByProjectId(Long projectId);
}
