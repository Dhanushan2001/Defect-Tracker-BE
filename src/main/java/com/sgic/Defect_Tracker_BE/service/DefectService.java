package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.CreateDefectRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectDto;
import com.sgic.Defect_Tracker_BE.dto.DefectHistoryDto;

import java.util.List;

public interface DefectService {

    DefectDto createDefect(CreateDefectRequest request);

    DefectDto updateDefect(Long id, CreateDefectRequest request);

    DefectDto getDefectById(Long id);

    List<DefectDto> getDefectsByProjectId(Long projectId);

    List<DefectDto> getDefectsByReleaseId(Long releaseId);

    List<DefectDto> getAllDefects();

    void deleteDefect(Long id);

    List<DefectHistoryDto> getDefectHistory(Long defectId);
}
