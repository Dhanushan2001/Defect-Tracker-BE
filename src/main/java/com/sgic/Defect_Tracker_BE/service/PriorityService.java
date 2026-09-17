package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreatePriorityRequest;
import com.sgic.Defect_Tracker_BE.dto.PriorityDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PriorityService {
    PaginatedData<PriorityDto> getAllPriorities(Pageable pageable);
    List<PriorityDto> getAllPrioritiesList();
    PriorityDto getPriorityById(Long id);
    PriorityDto createPriority(CreatePriorityRequest request);
    PriorityDto updatePriority(Long id, CreatePriorityRequest request);
    void deletePriority(Long id);
}
