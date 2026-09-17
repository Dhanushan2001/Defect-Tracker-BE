package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateDesignationRequest;
import com.sgic.Defect_Tracker_BE.dto.DesignationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DesignationService {
    PaginatedData<DesignationDto> getAllDesignations(Pageable pageable);
    List<DesignationDto> getAllDesignationsList();
    DesignationDto getDesignationById(Long id);
    DesignationDto createDesignation(CreateDesignationRequest request);
    DesignationDto updateDesignation(Long id, CreateDesignationRequest request);
    void deleteDesignation(Long id);
}
