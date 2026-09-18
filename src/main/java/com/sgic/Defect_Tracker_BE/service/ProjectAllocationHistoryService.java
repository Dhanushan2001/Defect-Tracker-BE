package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationHistoryDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocationHistory;
import com.sgic.Defect_Tracker_BE.entity.Role;

import java.time.LocalDate;
import java.util.List;

public interface ProjectAllocationHistoryService {

    List<ProjectAllocationHistoryDto> getHistoryByProjectId(Long projectId);

    List<ProjectAllocationHistoryDto> getHistoryByEmployeeId(Long employeeId);

    List<ProjectAllocationHistoryDto> getAllHistory();

    ProjectAllocationHistory recordHistory(Project project, Employee employee, Role role,
                                          Integer percentage, LocalDate startDate, LocalDate endDate,
                                          String action, Boolean status);

    void recordFromAllocation(ProjectAllocation allocation, String action, Boolean status);
}
