package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.TestCasePayloadDto;
import com.sgic.Defect_Tracker_BE.dto.TestCaseResponseDto;
import com.sgic.Defect_Tracker_BE.entity.*;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.mapper.TestCaseMapper;
import com.sgic.Defect_Tracker_BE.repository.*;
import com.sgic.Defect_Tracker_BE.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final SubModuleRepository subModuleRepository;
    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;
    private final SeverityRepository severityRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final TestCaseMapper testCaseMapper;

    @Override
    public TestCaseResponseDto createTestCase(Long subModuleId, TestCasePayloadDto payload) {
        if (payload == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Long targetSubModuleId = subModuleId != null ? subModuleId : payload.getSubModuleId();
        if (targetSubModuleId == null) {
            throw new BadRequestException("Submodule ID is required to create a test case");
        }

        if (payload.getDescription() == null || payload.getDescription().isBlank()) {
            throw new BadRequestException("Test case description is required");
        }

        SubModule subModule = subModuleRepository.findById(targetSubModuleId)
                .orElseThrow(() -> new ResourceNotFoundException("SubModule not found with id: " + targetSubModuleId));

        Module module = subModule.getModule();
        if (module == null && payload.getModuleId() != null) {
            module = moduleRepository.findById(payload.getModuleId()).orElse(null);
        }

        Project project = subModule.getProject();
        if (project == null && module != null) {
            project = module.getProject();
        }
        if (project == null && payload.getProjectId() != null) {
            project = projectRepository.findById(payload.getProjectId()).orElse(null);
        }

        if (module == null) {
            throw new BadRequestException("Could not resolve Module for SubModule id: " + targetSubModuleId);
        }
        if (project == null) {
            throw new BadRequestException("Could not resolve Project for SubModule id: " + targetSubModuleId);
        }

        Severity severity = null;
        if (payload.getSeverityId() != null) {
            severity = severityRepository.findById(payload.getSeverityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Severity not found with id: " + payload.getSeverityId()));
        }

        DefectType defectType = null;
        if (payload.getDefectTypeId() != null) {
            defectType = defectTypeRepository.findById(payload.getDefectTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("DefectType not found with id: " + payload.getDefectTypeId()));
        }

        TestCase testCase = TestCase.builder()
                .testcaseNo(payload.getTestcaseNo())
                .description(payload.getDescription().trim())
                .detailsSteps(payload.resolveSteps())
                .expectedResult(payload.getExpectedResult())
                .subModule(subModule)
                .module(module)
                .project(project)
                .severity(severity)
                .defectType(defectType)
                .executionStatus(payload.getExecutionStatus() != null ? payload.getExecutionStatus() : "NOT_RUN")
                .build();

        TestCase saved = testCaseRepository.save(testCase);

        if (saved.getTestcaseNo() == null || saved.getTestcaseNo().isBlank()) {
            saved.setTestcaseNo(String.format("TC-%03d", saved.getId()));
            saved = testCaseRepository.save(saved);
        }

        log.info("Created test case id {} ({}) for submodule {}", saved.getId(), saved.getTestcaseNo(), targetSubModuleId);
        return testCaseMapper.toDto(saved);
    }

    @Override
    public TestCaseResponseDto updateTestCase(Long id, TestCasePayloadDto payload) {
        if (id == null) {
            throw new BadRequestException("Test case ID cannot be null");
        }
        if (payload == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));

        if (payload.getDescription() != null && !payload.getDescription().isBlank()) {
            testCase.setDescription(payload.getDescription().trim());
        }

        if (payload.getDetailsSteps() != null || payload.getSteps() != null) {
            testCase.setDetailsSteps(payload.resolveSteps());
        }

        if (payload.getExpectedResult() != null) {
            testCase.setExpectedResult(payload.getExpectedResult());
        }

        if (payload.getSeverityId() != null) {
            Severity severity = severityRepository.findById(payload.getSeverityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Severity not found with id: " + payload.getSeverityId()));
            testCase.setSeverity(severity);
        }

        if (payload.getDefectTypeId() != null) {
            DefectType defectType = defectTypeRepository.findById(payload.getDefectTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("DefectType not found with id: " + payload.getDefectTypeId()));
            testCase.setDefectType(defectType);
        }

        if (payload.getSubModuleId() != null && !payload.getSubModuleId().equals(testCase.getSubModule().getId())) {
            SubModule newSubModule = subModuleRepository.findById(payload.getSubModuleId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubModule not found with id: " + payload.getSubModuleId()));
            testCase.setSubModule(newSubModule);
            if (newSubModule.getModule() != null) {
                testCase.setModule(newSubModule.getModule());
            }
            if (newSubModule.getProject() != null) {
                testCase.setProject(newSubModule.getProject());
            }
        }

        if (payload.getTestcaseNo() != null && !payload.getTestcaseNo().isBlank()) {
            testCase.setTestcaseNo(payload.getTestcaseNo());
        }

        if (payload.getExecutionStatus() != null && !payload.getExecutionStatus().isBlank()) {
            testCase.setExecutionStatus(payload.getExecutionStatus());
        }

        TestCase updated = testCaseRepository.save(testCase);
        log.info("Updated test case id {}", updated.getId());
        return testCaseMapper.toDto(updated);
    }

    @Override
    public void deleteTestCase(Long id) {
        if (id == null) {
            throw new BadRequestException("Test case ID cannot be null");
        }
        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));

        testCaseRepository.delete(testCase);
        log.info("Deleted test case id {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public TestCaseResponseDto getTestCaseById(Long id) {
        if (id == null) {
            throw new BadRequestException("Test case ID cannot be null");
        }
        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));
        return testCaseMapper.toDto(testCase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseResponseDto> getTestCasesBySubModule(Long subModuleId, String description, Long defectTypeId, Long severityId) {
        if (subModuleId == null) {
            return Collections.emptyList();
        }

        boolean hasFilters = (description != null && !description.isBlank())
                || (defectTypeId != null && defectTypeId > 0)
                || (severityId != null && severityId > 0);

        List<TestCase> list;
        if (hasFilters) {
            String descFilter = (description != null && !description.isBlank()) ? description.trim() : null;
            Long dtFilter = (defectTypeId != null && defectTypeId > 0) ? defectTypeId : null;
            Long sevFilter = (severityId != null && severityId > 0) ? severityId : null;
            list = testCaseRepository.searchTestCases(subModuleId, descFilter, dtFilter, sevFilter);
        } else {
            list = testCaseRepository.findBySubModuleIdOrderByIdAsc(subModuleId);
        }

        return testCaseMapper.toDtoList(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseResponseDto> getTestCasesByModuleId(Long moduleId) {
        if (moduleId == null) {
            return Collections.emptyList();
        }
        return testCaseMapper.toDtoList(testCaseRepository.findByModuleIdOrderByIdAsc(moduleId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseResponseDto> getTestCasesByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        return testCaseMapper.toDtoList(testCaseRepository.findByProjectIdOrderByIdAsc(projectId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseResponseDto> getAllTestCases() {
        return testCaseMapper.toDtoList(testCaseRepository.findAll());
    }
}
