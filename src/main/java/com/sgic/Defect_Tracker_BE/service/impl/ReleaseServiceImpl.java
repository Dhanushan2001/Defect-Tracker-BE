package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.CreateReleaseRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseDto;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.Release;
import com.sgic.Defect_Tracker_BE.entity.ReleaseType;
import com.sgic.Defect_Tracker_BE.mapper.ReleaseMapper;
import com.sgic.Defect_Tracker_BE.repository.ProjectRepository;
import com.sgic.Defect_Tracker_BE.repository.ReleaseRepository;
import com.sgic.Defect_Tracker_BE.repository.ReleaseTypeRepository;
import com.sgic.Defect_Tracker_BE.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final ReleaseTypeRepository releaseTypeRepository;
    private final ReleaseMapper releaseMapper;

    @Override
    public ReleaseDto createRelease(Long projectId, CreateReleaseRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Long effectiveProjectId = projectId != null ? projectId : request.resolveProjectId();
        if (effectiveProjectId == null) {
            throw new BadRequestException("Project ID is required to create a release");
        }

        Project project = projectRepository.findById(effectiveProjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + effectiveProjectId));

        String name = request.resolveName();
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Release name is required");
        }

        String trimmedName = name.trim();
        if (releaseRepository.existsByProjectIdAndNameIgnoreCase(effectiveProjectId, trimmedName)) {
            throw new BadRequestException("Release with name '" + trimmedName + "' already exists in this project");
        }

        ReleaseType releaseType = null;
        Long releaseTypeId = request.resolveReleaseTypeId();
        if (releaseTypeId != null) {
            releaseType = releaseTypeRepository.findById(releaseTypeId)
                    .orElse(null);
        } else if (request.getReleaseTypeName() != null && !request.getReleaseTypeName().trim().isEmpty()) {
            releaseType = releaseTypeRepository.findByReleaseTypeNameIgnoreCase(request.getReleaseTypeName().trim())
                    .orElse(null);
        }

        Release entity = releaseMapper.toEntity(request, project, releaseType);
        entity.setName(trimmedName);
        Release saved = releaseRepository.save(entity);
        return releaseMapper.toDto(saved);
    }

    @Override
    public ReleaseDto updateRelease(Long id, CreateReleaseRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found with id: " + id));

        String name = request.resolveName();
        if (name != null && !name.trim().isEmpty()) {
            String trimmedName = name.trim();
            Long projId = release.getProject() != null ? release.getProject().getId() : null;
            if (projId != null && releaseRepository.existsByProjectIdAndNameIgnoreCaseAndIdNot(projId, trimmedName, id)) {
                throw new BadRequestException("Release with name '" + trimmedName + "' already exists in this project");
            }
        }

        Project project = null;
        Long reqProjectId = request.resolveProjectId();
        if (reqProjectId != null) {
            project = projectRepository.findById(reqProjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + reqProjectId));
        }

        ReleaseType releaseType = null;
        Long releaseTypeId = request.resolveReleaseTypeId();
        if (releaseTypeId != null) {
            releaseType = releaseTypeRepository.findById(releaseTypeId)
                    .orElse(null);
        } else if (request.getReleaseTypeName() != null && !request.getReleaseTypeName().trim().isEmpty()) {
            releaseType = releaseTypeRepository.findByReleaseTypeNameIgnoreCase(request.getReleaseTypeName().trim())
                    .orElse(null);
        }

        releaseMapper.updateEntity(release, request, project, releaseType);
        Release updated = releaseRepository.save(release);
        return releaseMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseDto getReleaseById(Long id) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found with id: " + id));
        return releaseMapper.toDto(release);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseDto> getReleasesByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        List<Release> releases = releaseRepository.findByProjectIdOrderByIdAsc(projectId);
        return releaseMapper.toDtoList(releases);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseDto> getActiveReleasesByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        List<Release> releases = releaseRepository.findByProjectIdOrderByIdAsc(projectId);
        // Exclude releases marked as Completed/Inactive if any
        return releaseMapper.toDtoList(
                releases.stream()
                        .filter(r -> r.getStatus() == null || !"completed".equalsIgnoreCase(r.getStatus()))
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseDto> getAllReleases() {
        return releaseMapper.toDtoList(releaseRepository.findAllByOrderByIdAsc());
    }

    @Override
    public ReleaseDto updateReleaseStatus(Long id, String status) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found with id: " + id));

        String newStatus = (status != null && !status.trim().isEmpty()) ? status.trim().toUpperCase() : "HOLD";

        if ("ACTIVE".equalsIgnoreCase(newStatus)) {
            // When setting to ACTIVE, any other release in the same project is set to HOLD
            if (release.getProject() != null) {
                List<Release> projectReleases = releaseRepository.findByProjectIdOrderByIdAsc(release.getProject().getId());
                for (Release other : projectReleases) {
                    if (!other.getId().equals(id) && "ACTIVE".equalsIgnoreCase(other.getStatus())) {
                        other.setStatus("HOLD");
                        releaseRepository.save(other);
                    }
                }
            }
        }

        release.setStatus(newStatus);
        Release saved = releaseRepository.save(release);
        return releaseMapper.toDto(saved);
    }

    @Override
    public void deleteRelease(Long id) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found with id: " + id));
        releaseRepository.delete(release);
    }
}
