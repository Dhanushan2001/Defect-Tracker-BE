package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateDefectTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectTypeDto;
import com.sgic.Defect_Tracker_BE.entity.DefectType;
import com.sgic.Defect_Tracker_BE.mapper.DefectTypeMapper;
import com.sgic.Defect_Tracker_BE.repository.DefectTypeRepository;
import com.sgic.Defect_Tracker_BE.service.DefectTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DefectTypeServiceImpl implements DefectTypeService {

    private final DefectTypeRepository defectTypeRepository;
    private final DefectTypeMapper defectTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<DefectTypeDto> getAllDefectTypes(Pageable pageable) {
        Page<DefectType> page = defectTypeRepository.findAll(pageable);
        return PaginatedData.<DefectTypeDto>builder()
                .content(defectTypeMapper.toDtoList(page.getContent()))
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
    public List<DefectTypeDto> getAllDefectTypesList() {
        return defectTypeMapper.toDtoList(defectTypeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DefectTypeDto getDefectTypeById(Long id) {
        DefectType defectType = defectTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect Type not found with id: " + id));
        return defectTypeMapper.toDto(defectType);
    }

    @Override
    public DefectTypeDto createDefectType(CreateDefectTypeRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Defect Type name cannot be empty");
        }

        if (defectTypeRepository.existsByDefectTypeNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Defect Type already exists with name: " + name);
        }

        DefectType defectType = defectTypeMapper.toEntity(request);
        DefectType saved = defectTypeRepository.save(defectType);
        return defectTypeMapper.toDto(saved);
    }

    @Override
    public DefectTypeDto updateDefectType(Long id, CreateDefectTypeRequest request) {
        DefectType defectType = defectTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect Type not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Defect Type name cannot be empty");
        }

        if (defectTypeRepository.existsByDefectTypeNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Defect Type already exists with name: " + name);
        }

        defectTypeMapper.updateEntity(defectType, request);
        DefectType updated = defectTypeRepository.save(defectType);
        return defectTypeMapper.toDto(updated);
    }

    @Override
    public void deleteDefectType(Long id) {
        if (!defectTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Defect Type not found with id: " + id);
        }
        defectTypeRepository.deleteById(id);
    }
}
