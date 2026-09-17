package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.common.response.PaginatedData;
import com.sgic.Defect_Tracker_BE.dto.CreateStatusTypeRequest;
import com.sgic.Defect_Tracker_BE.dto.StatusTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StatusTypeService {
    PaginatedData<StatusTypeDto> getAllStatusTypes(Pageable pageable);
    List<StatusTypeDto> getAllStatusTypesList();
    StatusTypeDto getStatusTypeById(Long id);
    StatusTypeDto createStatusType(CreateStatusTypeRequest request);
    StatusTypeDto updateStatusType(Long id, CreateStatusTypeRequest request);
    void deleteStatusType(Long id);
}
