package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.CreateSubModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDto;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.SubModule;
import com.sgic.Defect_Tracker_BE.mapper.SubModuleMapper;
import com.sgic.Defect_Tracker_BE.repository.ModuleRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectRepository;
import com.sgic.Defect_Tracker_BE.repository.SubModuleRepository;
import com.sgic.Defect_Tracker_BE.service.SubModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubModuleServiceImpl implements SubModuleService {

    private final SubModuleRepository subModuleRepository;
    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;
    private final SubModuleMapper subModuleMapper;

    @Override
    public SubModuleDto createSubModule(Long moduleId, CreateSubModuleRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Long effectiveModuleId = moduleId != null ? moduleId : request.getModuleId();
        if (effectiveModuleId == null) {
            throw new BadRequestException("Module ID is required to create a submodule");
        }

        Module module = moduleRepository.findById(effectiveModuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + effectiveModuleId));

        Project project = module.getProject();
        if (request.getProjectId() != null && !project.getId().equals(request.getProjectId())) {
            throw new BadRequestException("Specified project ID " + request.getProjectId() + " does not match module's project ID " + project.getId());
        }

        String name = request.resolveName();
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Submodule name is required");
        }

        String trimmedName = name.trim();
        if (subModuleRepository.existsByModuleIdAndNameIgnoreCase(effectiveModuleId, trimmedName)) {
            throw new BadRequestException("Submodule with name '" + trimmedName + "' already exists in this module");
        }

        SubModule entity = subModuleMapper.toEntity(request, module, project);
        entity.setName(trimmedName);
        SubModule saved = subModuleRepository.save(entity);
        return subModuleMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubModuleDto> getSubModulesByModuleId(Long moduleId) {
        if (moduleId == null) {
            return Collections.emptyList();
        }
        List<SubModule> subModules = subModuleRepository.findByModuleIdOrderByIdAsc(moduleId);
        return subModuleMapper.toDtoList(subModules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubModuleDto> getSubModulesByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        List<SubModule> subModules = subModuleRepository.findByProjectIdOrderByIdAsc(projectId);
        return subModuleMapper.toDtoList(subModules);
    }

    @Override
    @Transactional(readOnly = true)
    public SubModuleDto getSubModuleById(Long id) {
        SubModule subModule = subModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submodule not found with id: " + id));
        return subModuleMapper.toDto(subModule);
    }

    @Override
    public SubModuleDto updateSubModule(Long moduleId, Long id, CreateSubModuleRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        SubModule subModule = subModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submodule not found with id: " + id));

        if (moduleId != null && !subModule.getModule().getId().equals(moduleId)) {
            throw new BadRequestException("Submodule with id " + id + " does not belong to module id " + moduleId);
        }

        String name = request.resolveName();
        if (name != null && !name.trim().isEmpty()) {
            String trimmedName = name.trim();
            if (subModuleRepository.existsByModuleIdAndNameIgnoreCaseAndIdNot(subModule.getModule().getId(), trimmedName, id)) {
                throw new BadRequestException("Submodule with name '" + trimmedName + "' already exists in this module");
            }
            subModule.setName(trimmedName);
        }

        if (request.getDescription() != null) {
            subModule.setDescription(request.getDescription());
        }

        SubModule saved = subModuleRepository.save(subModule);
        return subModuleMapper.toDto(saved);
    }

    @Override
    public void deleteSubModule(Long moduleId, Long id) {
        SubModule subModule = subModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submodule not found with id: " + id));

        if (moduleId != null && !subModule.getModule().getId().equals(moduleId)) {
            throw new BadRequestException("Submodule with id " + id + " does not belong to module id " + moduleId);
        }

        subModuleRepository.delete(subModule);
    }
}
