package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateReleaseTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseTypeDto;
import com.sgic.Defect_Tracker_BE.entity.ReleaseType;
import com.sgic.Defect_Tracker_BE.mapper.ReleaseTypeMapper;
import com.sgic.Defect_Tracker_BE.repository.ReleaseTypeRepository;
import com.sgic.Defect_Tracker_BE.service.ReleaseTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReleaseTypeServiceImpl implements ReleaseTypeService {

    private final ReleaseTypeRepository releaseTypeRepository;
    private final ReleaseTypeMapper releaseTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<ReleaseTypeDto> getAllReleaseTypes(Pageable pageable) {
        Page<ReleaseType> page = releaseTypeRepository.findAll(pageable);
        return PaginatedData.<ReleaseTypeDto>builder()
                .content(releaseTypeMapper.toDtoList(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .size(page.getSize())
                .number(page.getNumber())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseTypeDto> getAllReleaseTypesList() {
        return releaseTypeMapper.toDtoList(releaseTypeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseTypeDto getReleaseTypeById(Long id) {
        ReleaseType releaseType = releaseTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release Type not found with id: " + id));
        return releaseTypeMapper.toDto(releaseType);
    }

    @Override
    public ReleaseTypeDto createReleaseType(CreateReleaseTypeRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Release Type name cannot be empty");
        }

        if (releaseTypeRepository.existsByReleaseTypeNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Release Type already exists with name: " + name);
        }

        ReleaseType releaseType = releaseTypeMapper.toEntity(request);
        ReleaseType saved = releaseTypeRepository.save(releaseType);
        return releaseTypeMapper.toDto(saved);
    }

    @Override
    public ReleaseTypeDto updateReleaseType(Long id, CreateReleaseTypeRequest request) {
        ReleaseType releaseType = releaseTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release Type not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Release Type name cannot be empty");
        }

        if (releaseTypeRepository.existsByReleaseTypeNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Release Type already exists with name: " + name);
        }

        releaseTypeMapper.updateEntity(releaseType, request);
        ReleaseType updated = releaseTypeRepository.save(releaseType);
        return releaseTypeMapper.toDto(updated);
    }

    @Override
    public void deleteReleaseType(Long id) {
        if (!releaseTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Release Type not found with id: " + id);
        }
        releaseTypeRepository.deleteById(id);
    }
}
