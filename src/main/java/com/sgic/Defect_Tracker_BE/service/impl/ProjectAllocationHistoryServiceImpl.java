package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.dto.ProjectAllocationHistoryDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocationHistory;
import com.sgic.Defect_Tracker_BE.entity.Role;
import com.sgic.Defect_Tracker_BE.mapper.ProjectAllocationHistoryMapper;
import com.sgic.Defect_Tracker_BE.repository.ProjectAllocationHistoryRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectAllocationRepository;
import com.sgic.Defect_Tracker_BE.service.ProjectAllocationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectAllocationHistoryServiceImpl implements ProjectAllocationHistoryService {

    private final ProjectAllocationHistoryRepository projectAllocationHistoryRepository;
    private final ProjectAllocationRepository projectAllocationRepository;
    private final ProjectAllocationHistoryMapper projectAllocationHistoryMapper;

    @Override
    @Transactional
    public List<ProjectAllocationHistoryDto> getHistoryByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }

        // Auto-sync active allocations if no history exists for them yet
        List<ProjectAllocation> activeAllocations = projectAllocationRepository.findByProjectIdAndIsActiveTrue(projectId);
        for (ProjectAllocation alloc : activeAllocations) {
            if (alloc.getEmployee() != null && !projectAllocationHistoryRepository.existsByProjectIdAndEmployeeId(projectId, alloc.getEmployee().getId())) {
                ProjectAllocationHistory initialHistory = projectAllocationHistoryMapper.fromAllocation(alloc, "ALLOCATED", true);
                projectAllocationHistoryRepository.save(initialHistory);
            }
        }

        // Auto-sync inactive allocations if no deallocation history exists for them yet
        List<ProjectAllocation> inactiveAllocations = projectAllocationRepository.findByProjectIdAndIsActiveFalse(projectId);
        for (ProjectAllocation alloc : inactiveAllocations) {
            if (alloc.getEmployee() != null && !projectAllocationHistoryRepository.existsByProjectIdAndEmployeeIdAndAction(projectId, alloc.getEmployee().getId(), "DEALLOCATED")) {
                ProjectAllocationHistory deallocHistory = projectAllocationHistoryMapper.fromAllocation(alloc, "DEALLOCATED", false);
                projectAllocationHistoryRepository.save(deallocHistory);
            }
        }

        List<ProjectAllocationHistory> histories = projectAllocationHistoryRepository.findByProjectIdOrderByIdDesc(projectId);
        return projectAllocationHistoryMapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAllocationHistoryDto> getHistoryByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return Collections.emptyList();
        }
        List<ProjectAllocationHistory> histories = projectAllocationHistoryRepository.findByEmployeeIdOrderByIdDesc(employeeId);
        return projectAllocationHistoryMapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAllocationHistoryDto> getAllHistory() {
        return projectAllocationHistoryMapper.toDtoList(projectAllocationHistoryRepository.findAll());
    }

    @Override
    public ProjectAllocationHistory recordHistory(Project project, Employee employee, Role role,
                                                  Integer percentage, LocalDate startDate, LocalDate endDate,
                                                  String action, Boolean status) {
        ProjectAllocationHistory history = ProjectAllocationHistory.builder()
                .project(project)
                .employee(employee)
                .role(role)
                .allocationPercentage(percentage != null ? percentage : 0)
                .startDate(startDate)
                .endDate(endDate)
                .action(action != null ? action : "ALLOCATED")
                .status(status != null ? status : true)
                .build();

        return projectAllocationHistoryRepository.save(history);
    }

    @Override
    public void recordFromAllocation(ProjectAllocation allocation, String action, Boolean status) {
        if (allocation == null) {
            return;
        }
        ProjectAllocationHistory history = projectAllocationHistoryMapper.fromAllocation(allocation, action, status);
        projectAllocationHistoryRepository.save(history);
    }
}
