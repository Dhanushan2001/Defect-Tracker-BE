package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.CreateReleaseRequest;
import com.sgic.Defect_Tracker_BE.dto.ReleaseDto;

import java.util.List;

public interface ReleaseService {

    ReleaseDto createRelease(Long projectId, CreateReleaseRequest request);

    ReleaseDto updateRelease(Long id, CreateReleaseRequest request);

    ReleaseDto getReleaseById(Long id);

    List<ReleaseDto> getReleasesByProjectId(Long projectId);

    List<ReleaseDto> getActiveReleasesByProjectId(Long projectId);

    List<ReleaseDto> getAllReleases();

    ReleaseDto updateReleaseStatus(Long id, String status);

    void deleteRelease(Long id);
}
