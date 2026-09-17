package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateSeverityRequest;
import com.sgic.Defect_Tracker_BE.dto.SeverityDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeverityService {
    PaginatedData<SeverityDto> getAllSeverities(Pageable pageable);
    List<SeverityDto> getAllSeveritiesList();
    SeverityDto getSeverityById(Long id);
    SeverityDto createSeverity(CreateSeverityRequest request);
    SeverityDto updateSeverity(Long id, CreateSeverityRequest request);
    void deleteSeverity(Long id);
}
