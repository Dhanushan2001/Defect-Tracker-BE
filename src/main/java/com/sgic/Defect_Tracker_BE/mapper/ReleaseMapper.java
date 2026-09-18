package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.CreateReleaseRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseDto;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.Release;
import com.sgic.Defect_Tracker_BE.entity.ReleaseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReleaseMapper {

    private final ReleaseTypeMapper releaseTypeMapper;
    private final com.sgic.Defect_Tracker_BE.repository.ReleaseTestAllocationRepository releaseTestAllocationRepository;

    public ReleaseDto toDto(Release entity) {
        if (entity == null) {
            return null;
        }

        Long projectId = entity.getProject() != null ? entity.getProject().getId() : null;
        String projectName = entity.getProject() != null ? entity.getProject().getName() : null;

        Long releaseTypeId = entity.getReleaseType() != null ? entity.getReleaseType().getId() : null;
        String releaseTypeName = entity.getReleaseType() != null ? entity.getReleaseType().getReleaseTypeName() : null;

        String status = entity.getStatus() != null ? entity.getStatus() : "In Progress";
        // After the release date, active release goes to Hold state
        if (entity.getReleaseDate() != null && entity.getReleaseDate().isBefore(LocalDate.now()) && !"COMPLETED".equalsIgnoreCase(status)) {
            status = "HOLD";
        }

        int testCaseCount = 0;
        if (entity.getId() != null) {
            testCaseCount = (int) releaseTestAllocationRepository.countByReleaseId(entity.getId());
        }

        return ReleaseDto.builder()
                .id(entity.getId())
                .releaseId(entity.getId() != null ? String.valueOf(entity.getId()) : null)
                .name(entity.getName())
                .releaseName(entity.getName())
                .version(entity.getVersion())
                .description(entity.getDescription())
                .releaseDate(entity.getReleaseDate() != null ? entity.getReleaseDate().toString() : null)
                .status(status)
                .projectId(projectId)
                .projectIdAlias(projectId)
                .projectName(projectName)
                .releaseTypeId(releaseTypeId)
                .releaseTypeIdAlias(releaseTypeId)
                .releaseTypeName(releaseTypeName)
                .releaseTypeNameAlias(releaseTypeName)
                .releaseType(releaseTypeMapper.toDto(entity.getReleaseType()))
                .testCaseCount(testCaseCount)
                .testCaseCountAlias(testCaseCount)
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null)
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null)
                .build();
    }

    public List<ReleaseDto> toDtoList(List<Release> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Release toEntity(CreateReleaseRequest request, Project project, ReleaseType releaseType) {
        if (request == null) {
            return null;
        }

        String status = (request.getStatus() != null && !request.getStatus().trim().isEmpty())
                ? request.getStatus().trim()
                : "In Progress";

        return Release.builder()
                .name(request.resolveName())
                .version(request.getVersion() != null ? request.getVersion().trim() : null)
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .releaseDate(request.resolveReleaseDate())
                .status(status)
                .project(project)
                .releaseType(releaseType)
                .build();
    }

    public void updateEntity(Release entity, CreateReleaseRequest request, Project project, ReleaseType releaseType) {
        if (entity == null || request == null) {
            return;
        }

        String name = request.resolveName();
        if (name != null && !name.trim().isEmpty()) {
            entity.setName(name);
        }

        if (request.getVersion() != null) {
            entity.setVersion(request.getVersion().trim());
        }

        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription().trim());
        }

        LocalDate releaseDate = request.resolveReleaseDate();
        if (releaseDate != null) {
            entity.setReleaseDate(releaseDate);
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            entity.setStatus(request.getStatus().trim());
        }

        if (project != null) {
            entity.setProject(project);
        }

        if (releaseType != null) {
            entity.setReleaseType(releaseType);
        }
    }
}
