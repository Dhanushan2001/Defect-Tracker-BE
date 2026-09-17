package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateSeverityRequest;
import com.sgic.Defect_Tracker_BE.dto.SeverityDto;
import com.sgic.Defect_Tracker_BE.entity.Severity;
import com.sgic.Defect_Tracker_BE.mapper.SeverityMapper;
import com.sgic.Defect_Tracker_BE.repository.SeverityRepository;
import com.sgic.Defect_Tracker_BE.service.SeverityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeverityServiceImpl implements SeverityService {

    private final SeverityRepository severityRepository;
    private final SeverityMapper severityMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<SeverityDto> getAllSeverities(Pageable pageable) {
        Page<Severity> page = severityRepository.findAll(pageable);
        return PaginatedData.<SeverityDto>builder()
                .content(severityMapper.toDtoList(page.getContent()))
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
    public List<SeverityDto> getAllSeveritiesList() {
        return severityMapper.toDtoList(severityRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public SeverityDto getSeverityById(Long id) {
        Severity severity = severityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Severity not found with id: " + id));
        return severityMapper.toDto(severity);
    }

    @Override
    public SeverityDto createSeverity(CreateSeverityRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Severity name cannot be empty");
        }

        if (severityRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Severity already exists with name: " + name);
        }

        Integer weight = request.getWeight() != null ? request.getWeight() : 1;
        if (severityRepository.existsByWeight(weight)) {
            throw new DuplicateResourceException("Severity weight already exists: " + weight);
        }

        Severity severity = severityMapper.toEntity(request);
        Severity saved = severityRepository.save(severity);
        return severityMapper.toDto(saved);
    }

    @Override
    public SeverityDto updateSeverity(Long id, CreateSeverityRequest request) {
        Severity severity = severityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Severity not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Severity name cannot be empty");
        }

        if (severityRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Severity already exists with name: " + name);
        }

        Integer weight = request.getWeight() != null ? request.getWeight() : severity.getWeight();
        if (weight != null && severityRepository.existsByWeightAndIdNot(weight, id)) {
            throw new DuplicateResourceException("Severity weight already exists: " + weight);
        }

        severityMapper.updateEntity(severity, request);
        Severity updated = severityRepository.save(severity);
        return severityMapper.toDto(updated);
    }

    @Override
    public void deleteSeverity(Long id) {
        if (!severityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Severity not found with id: " + id);
        }
        severityRepository.deleteById(id);
    }
}
