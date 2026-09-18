package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.TestCasePayloadDto;
import com.sgic.Defect_Tracker_BE.dto.TestCaseResponseDto;

import java.util.List;

public interface TestCaseService {

    TestCaseResponseDto createTestCase(Long subModuleId, TestCasePayloadDto payload);

    TestCaseResponseDto updateTestCase(Long id, TestCasePayloadDto payload);

    void deleteTestCase(Long id);

    TestCaseResponseDto getTestCaseById(Long id);

    List<TestCaseResponseDto> getTestCasesBySubModule(Long subModuleId, String description, Long defectTypeId, Long severityId);

    List<TestCaseResponseDto> getTestCasesByModuleId(Long moduleId);

    List<TestCaseResponseDto> getTestCasesByProjectId(Long projectId);

    List<TestCaseResponseDto> getAllTestCases();
}
