package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.ProjectAllocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAllocationHistoryRepository extends JpaRepository<ProjectAllocationHistory, Long> {

    List<ProjectAllocationHistory> findByProjectIdOrderByIdDesc(Long projectId);

    List<ProjectAllocationHistory> findByProjectIdOrderByIdAsc(Long projectId);

    List<ProjectAllocationHistory> findByProjectIdAndEmployeeIdOrderByIdDesc(Long projectId, Long employeeId);

    List<ProjectAllocationHistory> findByEmployeeIdOrderByIdDesc(Long employeeId);

    boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    boolean existsByProjectIdAndEmployeeIdAndAction(Long projectId, Long employeeId, String action);

    boolean existsByProjectId(Long projectId);
}
