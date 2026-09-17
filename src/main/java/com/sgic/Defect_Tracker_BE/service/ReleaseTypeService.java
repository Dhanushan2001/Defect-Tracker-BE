package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateReleaseTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReleaseTypeService {
    PaginatedData<ReleaseTypeDto> getAllReleaseTypes(Pageable pageable);
    List<ReleaseTypeDto> getAllReleaseTypesList();
    ReleaseTypeDto getReleaseTypeById(Long id);
    ReleaseTypeDto createReleaseType(CreateReleaseTypeRequest request);
    ReleaseTypeDto updateReleaseType(Long id, CreateReleaseTypeRequest request);
    void deleteReleaseType(Long id);
}
