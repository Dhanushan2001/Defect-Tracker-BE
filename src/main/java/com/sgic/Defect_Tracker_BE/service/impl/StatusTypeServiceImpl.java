package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateStatusTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.StatusTypeDto;
import com.sgic.Defect_Tracker_BE.entity.StatusType;
import com.sgic.Defect_Tracker_BE.mapper.StatusTypeMapper;
import com.sgic.Defect_Tracker_BE.repository.StatusTypeRepository;
import com.sgic.Defect_Tracker_BE.repository.StatusWorkflowRepository;
import com.sgic.Defect_Tracker_BE.service.StatusTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatusTypeServiceImpl implements StatusTypeService {

    private final StatusTypeRepository statusTypeRepository;
    private final StatusWorkflowRepository statusWorkflowRepository;
    private final StatusTypeMapper statusTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<StatusTypeDto> getAllStatusTypes(Pageable pageable) {
        Page<StatusType> page = statusTypeRepository.findAll(pageable);
        return PaginatedData.<StatusTypeDto>builder()
                .content(statusTypeMapper.toDtoList(page.getContent()))
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
    public List<StatusTypeDto> getAllStatusTypesList() {
        return statusTypeMapper.toDtoList(statusTypeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public StatusTypeDto getStatusTypeById(Long id) {
        StatusType statusType = statusTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status Type not found with id: " + id));
        return statusTypeMapper.toDto(statusType);
    }

    @Override
    public StatusTypeDto createStatusType(CreateStatusTypeRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Status Type name cannot be empty");
        }

        if (statusTypeRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Status Type already exists with name: " + name);
        }

        StatusType statusType = statusTypeMapper.toEntity(request);
        StatusType saved = statusTypeRepository.save(statusType);
        return statusTypeMapper.toDto(saved);
    }

    @Override
    public StatusTypeDto updateStatusType(Long id, CreateStatusTypeRequest request) {
        StatusType statusType = statusTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status Type not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Status Type name cannot be empty");
        }

        if (statusTypeRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Status Type already exists with name: " + name);
        }

        statusTypeMapper.updateEntity(statusType, request);
        StatusType updated = statusTypeRepository.save(statusType);
        return statusTypeMapper.toDto(updated);
    }

    @Override
    public void deleteStatusType(Long id) {
        if (!statusTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Status Type not found with id: " + id);
        }
        statusWorkflowRepository.deleteByStatusId(id);
        statusTypeRepository.deleteById(id);
    }
}
