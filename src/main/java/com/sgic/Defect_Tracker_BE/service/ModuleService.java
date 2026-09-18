package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.CreateModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.ModuleDto;

import java.util.List;

public interface ModuleService {

    ModuleDto createModule(Long projectId, CreateModuleRequest request);

    List<ModuleDto> getModulesByProjectId(Long projectId);

    List<ModuleDto> getAllModules();

    ModuleDto getModuleById(Long id);

    ModuleDto updateModule(Long projectId, Long id, CreateModuleRequest request);

    void deleteModule(Long projectId, Long id);
}
