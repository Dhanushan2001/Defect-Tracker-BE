package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.ProjectQaMemberDto;
import com.sgic.Defect_Tracker_BE.dto.ReleaseTestAllocationResponseDto;

import java.util.List;
import java.util.Map;

public interface ReleaseTestAllocationService {

    // 1:1 mode
    ReleaseTestAllocationResponseDto allocateTestCaseToRelease(Long releaseId, Long testCaseId);

    // 1:M mode
    Map<String, Object> allocateTestCaseToMultipleReleases(Long testCaseId, List<Long> releaseIds);

    // Bulk mode (many test cases to 1 release)
    List<ReleaseTestAllocationResponseDto> bulkAllocateTestCases(Long releaseId, List<Long> testCaseIds);

    // M:M mode (many test cases to many releases)
    List<Map<String, Object>> allocateManyToMany(List<Long> releaseIds, List<Long> testCaseIds);

    // Get allocated test cases for a release with optional filters
    List<ReleaseTestAllocationResponseDto> getReleaseAllocatedTestCases(Long releaseId, Long moduleId, Long subModuleId);

    // Deallocate test case from release
    void deallocateTestCase(Long releaseId, Long testCaseId);

    // QA members allocated to project
    List<ProjectQaMemberDto> getProjectQaMembers(Long projectId);

    // Assign / reassign QA to release test cases
    Map<String, Object> bulkAssignQa(Long releaseId, Long qaId, List<Long> testCaseIds);

    // Update execution status (PASS, FAIL, NOT_RUN)
    ReleaseTestAllocationResponseDto updateExecutionStatus(Long releaseId, Long testCaseId, String status);
}
