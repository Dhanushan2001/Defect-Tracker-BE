package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.CreateSubModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDto;

import java.util.List;

public interface SubModuleService {

    SubModuleDto createSubModule(Long moduleId, CreateSubModuleRequest request);

    List<SubModuleDto> getSubModulesByModuleId(Long moduleId);

    List<SubModuleDto> getSubModulesByProjectId(Long projectId);

    SubModuleDto getSubModuleById(Long id);

    SubModuleDto updateSubModule(Long moduleId, Long id, CreateSubModuleRequest request);

    void deleteSubModule(Long moduleId, Long id);
}
