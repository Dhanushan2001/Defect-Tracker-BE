package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectAllocationRepository extends JpaRepository<ProjectAllocation, Long> {

    List<ProjectAllocation> findByProjectIdAndIsActiveTrue(Long projectId);

    List<ProjectAllocation> findByProjectIdAndIsActiveFalse(Long projectId);

    List<ProjectAllocation> findByEmployeeIdAndIsActiveTrue(Long employeeId);

    Optional<ProjectAllocation> findByProjectIdAndEmployeeIdAndIsActiveTrue(Long projectId, Long employeeId);

    Optional<ProjectAllocation> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    List<ProjectAllocation> findByIsActiveTrue();
}
