package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.CreateModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.ModuleDto;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.mapper.ModuleMapper;
import com.sgic.Defect_Tracker_BE.repository.ModuleRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectRepository;
import com.sgic.Defect_Tracker_BE.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;
    private final ModuleMapper moduleMapper;

    @Override
    public ModuleDto createModule(Long projectId, CreateModuleRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Long effectiveProjectId = projectId != null ? projectId : request.getProjectId();
        if (effectiveProjectId == null) {
            throw new BadRequestException("Project ID is required to create a module");
        }

        Project project = projectRepository.findById(effectiveProjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + effectiveProjectId));

        String name = request.resolveName();
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Module name is required");
        }

        String trimmedName = name.trim();
        if (moduleRepository.existsByProjectIdAndNameIgnoreCase(effectiveProjectId, trimmedName)) {
            throw new BadRequestException("Module with name '" + trimmedName + "' already exists in this project");
        }

        Module entity = moduleMapper.toEntity(request, project);
        entity.setName(trimmedName);
        Module saved = moduleRepository.save(entity);
        return moduleMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getModulesByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        List<Module> modules = moduleRepository.findByProjectIdOrderByIdAsc(projectId);
        return moduleMapper.toDtoList(modules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getAllModules() {
        return moduleMapper.toDtoList(moduleRepository.findAllByOrderByIdAsc());
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        return moduleMapper.toDto(module);
    }

    @Override
    public ModuleDto updateModule(Long projectId, Long id, CreateModuleRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));

        if (projectId != null && !module.getProject().getId().equals(projectId)) {
            throw new BadRequestException("Module with id " + id + " does not belong to project id " + projectId);
        }

        String name = request.resolveName();
        if (name != null && !name.trim().isEmpty()) {
            String trimmedName = name.trim();
            if (moduleRepository.existsByProjectIdAndNameIgnoreCaseAndIdNot(module.getProject().getId(), trimmedName, id)) {
                throw new BadRequestException("Module with name '" + trimmedName + "' already exists in this project");
            }
            module.setName(trimmedName);
        }

        if (request.getDescription() != null) {
            module.setDescription(request.getDescription());
        }

        Module saved = moduleRepository.save(module);
        return moduleMapper.toDto(saved);
    }

    @Override
    public void deleteModule(Long projectId, Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));

        if (projectId != null && !module.getProject().getId().equals(projectId)) {
            throw new BadRequestException("Module with id " + id + " does not belong to project id " + projectId);
        }

        moduleRepository.delete(module);
    }
}
