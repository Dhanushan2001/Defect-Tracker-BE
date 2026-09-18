package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateSubModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.SubModuleDto;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.SubModule;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubModuleMapper {

    public SubModuleDto toDto(SubModule entity) {
        if (entity == null) {
            return null;
        }

        Long moduleId = entity.getModule() != null ? entity.getModule().getId() : null;
        String moduleName = entity.getModule() != null ? entity.getModule().getName() : null;
        Long projectId = entity.getProject() != null ? entity.getProject().getId() : null;
        String projectName = entity.getProject() != null ? entity.getProject().getName() : null;

        return SubModuleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .subModuleName(entity.getName())
                .description(entity.getDescription())
                .moduleId(moduleId)
                .moduleName(moduleName)
                .projectId(projectId)
                .projectName(projectName)
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null)
                .build();
    }

    public List<SubModuleDto> toDtoList(List<SubModule> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public SubModule toEntity(CreateSubModuleRequest request, Module module, Project project) {
        if (request == null) {
            return null;
        }

        return SubModule.builder()
                .name(request.resolveName())
                .description(request.getDescription())
                .module(module)
                .project(project)
                .build();
    }

    public void updateEntity(SubModule entity, CreateSubModuleRequest request) {
        if (entity == null || request == null) {
            return;
        }

        String name = request.resolveName();
        if (name != null) {
            entity.setName(name);
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
    }
}
