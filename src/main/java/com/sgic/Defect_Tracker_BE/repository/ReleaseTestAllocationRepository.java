package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.ReleaseTestAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReleaseTestAllocationRepository extends JpaRepository<ReleaseTestAllocation, Long> {

    boolean existsByReleaseIdAndTestCaseId(Long releaseId, Long testCaseId);

    Optional<ReleaseTestAllocation> findByReleaseIdAndTestCaseId(Long releaseId, Long testCaseId);

    List<ReleaseTestAllocation> findByReleaseIdOrderByIdAsc(Long releaseId);

    List<ReleaseTestAllocation> findByReleaseIdAndModuleIdOrderByIdAsc(Long releaseId, Long moduleId);

    List<ReleaseTestAllocation> findByReleaseIdAndSubModuleIdOrderByIdAsc(Long releaseId, Long subModuleId);

    List<ReleaseTestAllocation> findByReleaseIdAndModuleIdAndSubModuleIdOrderByIdAsc(Long releaseId, Long moduleId, Long subModuleId);

    List<ReleaseTestAllocation> findByProjectIdOrderByIdAsc(Long projectId);

    List<ReleaseTestAllocation> findByReleaseIdAndAssignedQaIdOrderByIdAsc(Long releaseId, Long assignedQaId);

    long countByReleaseId(Long releaseId);

    void deleteByReleaseIdAndTestCaseId(Long releaseId, Long testCaseId);
}
