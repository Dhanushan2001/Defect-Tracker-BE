package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.DuplicateResourceException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreatePriorityRequest;
import com.sgic.Defect_Tracker_BE.dto.PriorityDto;
import com.sgic.Defect_Tracker_BE.entity.Priority;
import com.sgic.Defect_Tracker_BE.mapper.PriorityMapper;
import com.sgic.Defect_Tracker_BE.repository.PriorityRepository;
import com.sgic.Defect_Tracker_BE.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;
    private final PriorityMapper priorityMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<PriorityDto> getAllPriorities(Pageable pageable) {
        Page<Priority> page = priorityRepository.findAll(pageable);
        return PaginatedData.<PriorityDto>builder()
                .content(priorityMapper.toDtoList(page.getContent()))
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
    public List<PriorityDto> getAllPrioritiesList() {
        return priorityMapper.toDtoList(priorityRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PriorityDto getPriorityById(Long id) {
        Priority priority = priorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + id));
        return priorityMapper.toDto(priority);
    }

    @Override
    public PriorityDto createPriority(CreatePriorityRequest request) {
        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Priority name cannot be empty");
        }

        if (priorityRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Priority already exists with name: " + name);
        }

        Priority priority = priorityMapper.toEntity(request);
        Priority saved = priorityRepository.save(priority);
        return priorityMapper.toDto(saved);
    }

    @Override
    public PriorityDto updatePriority(Long id, CreatePriorityRequest request) {
        Priority priority = priorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + id));

        String name = request.resolveName();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Priority name cannot be empty");
        }

        if (priorityRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Priority already exists with name: " + name);
        }

        priorityMapper.updateEntity(priority, request);
        Priority updated = priorityRepository.save(priority);
        return priorityMapper.toDto(updated);
    }

    @Override
    public void deletePriority(Long id) {
        if (!priorityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Priority not found with id: " + id);
        }
        priorityRepository.deleteById(id);
    }
}
