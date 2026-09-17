package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateDesignationRequest;
import com.sgic.Defect_Tracker_BE.dto.DesignationDto;
import com.sgic.Defect_Tracker_BE.entity.Designation;
import com.sgic.Defect_Tracker_BE.mapper.DesignationMapper;
import com.sgic.Defect_Tracker_BE.repository.DesignationRepository;
import com.sgic.Defect_Tracker_BE.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationMapper designationMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<DesignationDto> getAllDesignations(Pageable pageable) {
        Page<Designation> page = designationRepository.findAll(pageable);
        return PaginatedData.<DesignationDto>builder()
                .content(designationMapper.toDtoList(page.getContent()))
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
    public List<DesignationDto> getAllDesignationsList() {
        return designationMapper.toDtoList(designationRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationDto getDesignationById(Long id) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + id));
        return designationMapper.toDto(designation);
    }

    @Override
    public DesignationDto createDesignation(CreateDesignationRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Designation name cannot be empty");
        }

        if (designationRepository.existsByDesignationNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Designation already exists with name: " + name);
        }

        Designation designation = designationMapper.toEntity(request);
        Designation saved = designationRepository.save(designation);
        return designationMapper.toDto(saved);
    }

    @Override
    public DesignationDto updateDesignation(Long id, CreateDesignationRequest request) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Designation name cannot be empty");
        }

        if (designationRepository.existsByDesignationNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Designation already exists with name: " + name);
        }

        designationMapper.updateEntity(designation, request);
        Designation updated = designationRepository.save(designation);
        return designationMapper.toDto(updated);
    }

    @Override
    public void deleteDesignation(Long id) {
        if (!designationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Designation not found with id: " + id);
        }
        designationRepository.deleteById(id);
    }
}
