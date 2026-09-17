package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateDefectTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DefectTypeService {
    PaginatedData<DefectTypeDto> getAllDefectTypes(Pageable pageable);
    List<DefectTypeDto> getAllDefectTypesList();
    DefectTypeDto getDefectTypeById(Long id);
    DefectTypeDto createDefectType(CreateDefectTypeRequest request);
    DefectTypeDto updateDefectType(Long id, CreateDefectTypeRequest request);
    void deleteDefectType(Long id);
}
