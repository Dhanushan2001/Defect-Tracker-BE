package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.AvailableManagerDto;
import com.sgic.Defect_Tracker_BE.dto.CreateProjectRequest;
import com.sgic.Defect_Tracker_BE.dto.ProjectDto;

import java.util.List;

public interface ProjectService {

    List<ProjectDto> getAllProjects();

    ProjectDto getProjectById(Long id);

    ProjectDto createProject(CreateProjectRequest request);

    ProjectDto updateProject(Long id, CreateProjectRequest request);

    void deleteProject(Long id);

    List<AvailableManagerDto> getAvailableManagers(Long designationId);
}
