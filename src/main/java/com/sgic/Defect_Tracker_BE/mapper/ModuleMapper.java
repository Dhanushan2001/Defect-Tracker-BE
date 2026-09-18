package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateModuleRequest;
import com.sgic.Defect_Tracker_BE.dto.ModuleDto;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModuleMapper {

    private final SubModuleMapper subModuleMapper;

    public ModuleDto toDto(Module entity) {
        if (entity == null) {
            return null;
        }

        Long projectId = entity.getProject() != null ? entity.getProject().getId() : null;
        String projectName = entity.getProject() != null ? entity.getProject().getName() : null;

        Long leaderId = null;
        String leaderName = null;
        ModuleDto.AssignedDev assignedDev = null;

        if (entity.getLeader() != null) {
            leaderId = entity.getLeader().getId();
            String fName = entity.getLeader().getFirstName() != null ? entity.getLeader().getFirstName() : "";
            String lName = entity.getLeader().getLastName() != null ? entity.getLeader().getLastName() : "";
            leaderName = (fName + " " + lName).trim();
            assignedDev = ModuleDto.AssignedDev.builder()
                    .userId(leaderId)
                    .userName(leaderName)
                    .build();
        }

        return ModuleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .moduleName(entity.getName())
                .description(entity.getDescription())
                .projectId(projectId)
                .projectName(projectName)
                .submodules(subModuleMapper.toDtoList(entity.getSubModules()))
                .leaderId(leaderId)
                .leaderName(leaderName)
                .assignedDev(assignedDev)
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null)
                .build();
    }

    public List<ModuleDto> toDtoList(List<Module> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Module toEntity(CreateModuleRequest request, Project project) {
        if (request == null) {
            return null;
        }

        return Module.builder()
                .name(request.resolveName())
                .description(request.getDescription())
                .project(project)
                .build();
    }

    public void updateEntity(Module entity, CreateModuleRequest request) {
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
