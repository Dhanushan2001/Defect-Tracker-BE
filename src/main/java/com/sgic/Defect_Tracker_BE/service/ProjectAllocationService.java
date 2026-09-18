package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationPayloadDto;
import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationResponseDto;

import java.util.List;

public interface ProjectAllocationService {

    List<ProjectAllocationResponseDto> getAllAllocations();

    List<ProjectAllocationResponseDto> getAllocationsByProjectId(Long projectId);

    List<ProjectAllocationResponseDto> getAllocationsByEmployeeId(Long employeeId);

    ProjectAllocationResponseDto allocate(ProjectAllocationPayloadDto payload);

    ProjectAllocationResponseDto updateAllocation(Long id, ProjectAllocationPayloadDto payload);

    void deallocate(Long allocationId);

    void deallocateByEmployeeId(Long employeeId);

    void deallocateByProjectAndEmployee(Long projectId, Long employeeId);
}
