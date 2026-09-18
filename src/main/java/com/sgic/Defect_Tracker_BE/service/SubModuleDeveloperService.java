package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.AllocateSubModuleDeveloperRequest;
import com.sgic.Defect_Tracker_BE.dto.EligibleLeaderDto;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDeveloperResponseDto;

import java.util.List;

public interface SubModuleDeveloperService {

    List<EligibleLeaderDto> getEligibleDevelopers(Long projectId);

    SubModuleDeveloperResponseDto allocateDeveloper(AllocateSubModuleDeveloperRequest request);

    SubModuleDeveloperResponseDto allocateDeveloper(Long subModuleId, Long employeeId);

    void deallocateDeveloper(Long subModuleId, Long employeeId);

    void deallocateById(Long id);

    List<SubModuleDeveloperResponseDto> getDevelopersBySubModuleId(Long subModuleId);

    List<SubModuleDeveloperResponseDto> getDevelopersByProjectId(Long projectId);
}
