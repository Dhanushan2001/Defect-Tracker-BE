package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.ProjectQaMemberDto;
import com.sgic.Defect_Tracker_BE.dto.ReleaseTestAllocationResponseDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.ProjectAllocation;
import com.sgic.Defect_Tracker_BE.entity.Release;
import com.sgic.Defect_Tracker_BE.entity.ReleaseTestAllocation;
import com.sgic.Defect_Tracker_BE.entity.Role;
import com.sgic.Defect_Tracker_BE.entity.SubModule;
import com.sgic.Defect_Tracker_BE.entity.TestCase;
import com.sgic.Defect_Tracker_BE.mapper.ReleaseTestAllocationMapper;
import com.sgic.Defect_Tracker_BE.repository.EmployeeRepository;
import com.sgic.Defect_Tracker_BE.repository.ProjectAllocationRepository;
import com.sgic.Defect_Tracker_BE.repository.ReleaseRepository;
import com.sgic.Defect_Tracker_BE.repository.ReleaseTestAllocationRepository;
import com.sgic.Defect_Tracker_BE.repository.TestCaseRepository;
import com.sgic.Defect_Tracker_BE.service.ReleaseTestAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReleaseTestAllocationServiceImpl implements ReleaseTestAllocationService {

    private final ReleaseTestAllocationRepository releaseTestAllocationRepository;
    private final ReleaseRepository releaseRepository;
    private final TestCaseRepository testCaseRepository;
    private final ProjectAllocationRepository projectAllocationRepository;
    private final EmployeeRepository employeeRepository;
    private final com.sgic.Defect_Tracker_BE.repository.DefectRepository defectRepository;

    private boolean isQaLeadOrQaEngineer(Role role) {
        if (role == null) {
            return false;
        }
        String name = role.getRoleName() != null ? role.getRoleName().trim().toLowerCase().replaceAll("[_\\s]+", "") : "";
        String type = role.getRoleType() != null ? role.getRoleType().trim().toLowerCase().replaceAll("[_\\s]+", "") : "";
        return name.contains("qalead") || name.contains("qaengineer") || name.equals("qa") ||
               type.contains("qalead") || type.contains("qaengineer") || type.equals("qa");
    }

    @Override
    public ReleaseTestAllocationResponseDto allocateTestCaseToRelease(Long releaseId, Long testCaseId) {
        if (releaseId == null) {
            throw new BadRequestException("Release ID must not be null");
        }
        if (testCaseId == null) {
            throw new BadRequestException("Test Case ID must not be null");
        }

        Release release = releaseRepository.findById(releaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found with id " + releaseId));

        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Test Case not found with id " + testCaseId));

        if (releaseTestAllocationRepository.existsByReleaseIdAndTestCaseId(releaseId, testCaseId)) {
            String tcNo = testCase.getTestcaseNo() != null ? testCase.getTestcaseNo() : String.valueOf(testCaseId);
            throw new BadRequestException("Test case " + tcNo + " is already allocated to this release.");
        }

        Project project = testCase.getProject() != null ? testCase.getProject() : release.getProject();
        Module module = testCase.getModule() != null ? testCase.getModule() :
                (testCase.getSubModule() != null ? testCase.getSubModule().getModule() : null);
        SubModule subModule = testCase.getSubModule();

        if (subModule == null || module == null || project == null) {
            throw new BadRequestException("Test Case must have SubModule, Module, and Project relationships properly defined.");
        }

        ReleaseTestAllocation allocation = ReleaseTestAllocation.builder()
                .release(release)
                .testCase(testCase)
                .project(project)
                .module(module)
                .subModule(subModule)
                .status("UNASSIGNED")
                .build();

        ReleaseTestAllocation saved = releaseTestAllocationRepository.save(allocation);
        log.info("Allocated test case {} to release {}", testCase.getId(), release.getId());
        return ReleaseTestAllocationMapper.toResponseDto(saved);
    }

    @Override
    public Map<String, Object> allocateTestCaseToMultipleReleases(Long testCaseId, List<Long> releaseIds) {
        if (testCaseId == null) {
            throw new BadRequestException("Test Case ID must not be null");
        }
        if (releaseIds == null || releaseIds.isEmpty()) {
            throw new BadRequestException("Release IDs list must not be empty");
        }

        TestCase testCase = testCaseRepository.findById(testCaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Test Case not found with id " + testCaseId));

        List<Map<String, Object>> results = new ArrayList<>();
        List<Map<String, Object>> failed = new ArrayList<>();

        for (Long releaseId : releaseIds) {
            if (releaseId == null) continue;

            Optional<Release> releaseOpt = releaseRepository.findById(releaseId);
            if (releaseOpt.isEmpty()) {
                failed.add(Map.of("releaseId", releaseId, "error", "Release with ID " + releaseId + " does not exist."));
                continue;
            }

            Release release = releaseOpt.get();
            if (releaseTestAllocationRepository.existsByReleaseIdAndTestCaseId(releaseId, testCaseId)) {
                String tcNo = testCase.getTestcaseNo() != null ? testCase.getTestcaseNo() : String.valueOf(testCaseId);
                failed.add(Map.of("releaseId", releaseId, "error", "Test case " + tcNo + " is already allocated to this release."));
                continue;
            }

            try {
                Project project = testCase.getProject() != null ? testCase.getProject() : release.getProject();
                Module module = testCase.getModule() != null ? testCase.getModule() :
                        (testCase.getSubModule() != null ? testCase.getSubModule().getModule() : null);
                SubModule subModule = testCase.getSubModule();

                ReleaseTestAllocation allocation = ReleaseTestAllocation.builder()
                        .release(release)
                        .testCase(testCase)
                        .project(project)
                        .module(module)
                        .subModule(subModule)
                        .status("UNASSIGNED")
                        .build();

                releaseTestAllocationRepository.save(allocation);
                results.add(Map.of("releaseId", releaseId, "status", "success"));
            } catch (Exception e) {
                log.error("Error allocating test case {} to release {}: {}", testCaseId, releaseId, e.getMessage());
                failed.add(Map.of("releaseId", releaseId, "error", e.getMessage() != null ? e.getMessage() : "Unknown error"));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("failed", failed);
        response.put("message", "Test case allocated to " + results.size() + " release(s) successfully.");
        return response;
    }

    @Override
    public List<ReleaseTestAllocationResponseDto> bulkAllocateTestCases(Long releaseId, List<Long> testCaseIds) {
        if (releaseId == null) {
            throw new BadRequestException("Release ID must not be null");
        }
        if (testCaseIds == null || testCaseIds.isEmpty()) {
            return Collections.emptyList();
        }

        Release release = releaseRepository.findById(releaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found with id " + releaseId));

        List<ReleaseTestAllocationResponseDto> allocated = new ArrayList<>();

        for (Long testCaseId : testCaseIds) {
            if (testCaseId == null) continue;
            if (releaseTestAllocationRepository.existsByReleaseIdAndTestCaseId(releaseId, testCaseId)) {
                // Already allocated, skip duplicate silently for bulk
                continue;
            }

            Optional<TestCase> tcOpt = testCaseRepository.findById(testCaseId);
            if (tcOpt.isEmpty()) continue;

            TestCase testCase = tcOpt.get();
            Project project = testCase.getProject() != null ? testCase.getProject() : release.getProject();
            Module module = testCase.getModule() != null ? testCase.getModule() :
                    (testCase.getSubModule() != null ? testCase.getSubModule().getModule() : null);
            SubModule subModule = testCase.getSubModule();

            if (subModule == null || module == null || project == null) {
                continue;
            }

            ReleaseTestAllocation allocation = ReleaseTestAllocation.builder()
                    .release(release)
                    .testCase(testCase)
                    .project(project)
                    .module(module)
                    .subModule(subModule)
                    .status("UNASSIGNED")
                    .build();

            ReleaseTestAllocation saved = releaseTestAllocationRepository.save(allocation);
            allocated.add(ReleaseTestAllocationMapper.toResponseDto(saved));
        }

        log.info("Bulk allocated {} new test cases to release {}", allocated.size(), releaseId);
        return allocated;
    }

    @Override
    public List<Map<String, Object>> allocateManyToMany(List<Long> releaseIds, List<Long> testCaseIds) {
        if (releaseIds == null || releaseIds.isEmpty() || testCaseIds == null || testCaseIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (Long releaseId : releaseIds) {
            if (releaseId == null) continue;

            Optional<Release> releaseOpt = releaseRepository.findById(releaseId);
            if (releaseOpt.isEmpty()) {
                Map<String, Object> item = new HashMap<>();
                item.put("releaseId", releaseId);
                item.put("releaseName", "Release " + releaseId);
                item.put("status", "rejected");
                item.put("error", Map.of("message", "Release not found with id " + releaseId));
                results.add(item);
                continue;
            }

            Release release = releaseOpt.get();
            String releaseName = release.getName() != null ? release.getName() : "Release " + releaseId;

            try {
                List<ReleaseTestAllocationResponseDto> newlyAllocated = bulkAllocateTestCases(releaseId, testCaseIds);
                Map<String, Object> item = new HashMap<>();
                item.put("releaseId", releaseId);
                item.put("releaseName", releaseName);
                item.put("status", "fulfilled");
                item.put("data", newlyAllocated);
                item.put("error", null);
                results.add(item);
            } catch (Exception e) {
                Map<String, Object> item = new HashMap<>();
                item.put("releaseId", releaseId);
                item.put("releaseName", releaseName);
                item.put("status", "rejected");
                item.put("error", Map.of("message", e.getMessage() != null ? e.getMessage() : "Unknown error"));
                results.add(item);
            }
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseTestAllocationResponseDto> getReleaseAllocatedTestCases(Long releaseId, Long moduleId, Long subModuleId) {
        if (releaseId == null) {
            return Collections.emptyList();
        }

        List<ReleaseTestAllocation> allocations;

        if (moduleId != null && subModuleId != null) {
            allocations = releaseTestAllocationRepository.findByReleaseIdAndModuleIdAndSubModuleIdOrderByIdAsc(releaseId, moduleId, subModuleId);
        } else if (subModuleId != null) {
            allocations = releaseTestAllocationRepository.findByReleaseIdAndSubModuleIdOrderByIdAsc(releaseId, subModuleId);
        } else if (moduleId != null) {
            allocations = releaseTestAllocationRepository.findByReleaseIdAndModuleIdOrderByIdAsc(releaseId, moduleId);
        } else {
            allocations = releaseTestAllocationRepository.findByReleaseIdOrderByIdAsc(releaseId);
        }

        return allocations.stream()
                .map(alloc -> {
                    com.sgic.Defect_Tracker_BE.entity.Defect defect = null;
                    if (alloc.getTestCase() != null) {
                        defect = defectRepository.findTopByReleaseIdAndTestCaseIdOrderByIdDesc(alloc.getRelease().getId(), alloc.getTestCase().getId())
                                .or(() -> defectRepository.findTopByTestCaseIdOrderByIdDesc(alloc.getTestCase().getId()))
                                .orElse(null);
                    }
                    return ReleaseTestAllocationMapper.toResponseDto(alloc, defect);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deallocateTestCase(Long releaseId, Long testCaseId) {
        if (releaseId != null && testCaseId != null) {
            releaseTestAllocationRepository.deleteByReleaseIdAndTestCaseId(releaseId, testCaseId);
            log.info("Deallocated test case {} from release {}", testCaseId, releaseId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectQaMemberDto> getProjectQaMembers(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }

        List<ProjectAllocation> allocations = projectAllocationRepository.findByProjectIdAndIsActiveTrue(projectId);
        Map<Long, ProjectQaMemberDto> qaMemberMap = new LinkedHashMap<>();

        for (ProjectAllocation alloc : allocations) {
            if (alloc.getEmployee() != null && isQaLeadOrQaEngineer(alloc.getRole())) {
                Employee emp = alloc.getEmployee();
                Role role = alloc.getRole();

                String first = emp.getFirstName() != null ? emp.getFirstName().trim() : "";
                String last = emp.getLastName() != null ? emp.getLastName().trim() : "";
                String fullName = (first + " " + last).trim();
                if (fullName.isEmpty()) {
                    fullName = "Employee " + emp.getId();
                }

                String roleName = role != null && role.getRoleName() != null ? role.getRoleName() : "QA Engineer";
                String designationName = emp.getDesignation() != null ? emp.getDesignation().getDesignationName() : null;

                qaMemberMap.putIfAbsent(emp.getId(), ProjectQaMemberDto.builder()
                        .id(emp.getId())
                        .userId(emp.getId())
                        .name(fullName)
                        .userFullName(fullName)
                        .roleName(roleName)
                        .designationName(designationName)
                        .build());
            }
        }

        return new ArrayList<>(qaMemberMap.values());
    }

    @Override
    public Map<String, Object> bulkAssignQa(Long releaseId, Long qaId, List<Long> testCaseIds) {
        if (qaId == null) {
            throw new BadRequestException("QA Member ID must not be null");
        }
        if (testCaseIds == null || testCaseIds.isEmpty()) {
            throw new BadRequestException("Test Case IDs must not be empty");
        }

        Employee qa = employeeRepository.findById(qaId)
                .orElseThrow(() -> new ResourceNotFoundException("QA Employee not found with id " + qaId));

        int updatedCount = 0;

        for (Long id : testCaseIds) {
            if (id == null) continue;

            List<ReleaseTestAllocation> toUpdate = new ArrayList<>();

            if (releaseId != null) {
                Optional<ReleaseTestAllocation> byRelAndTc = releaseTestAllocationRepository.findByReleaseIdAndTestCaseId(releaseId, id);
                if (byRelAndTc.isPresent()) {
                    toUpdate.add(byRelAndTc.get());
                } else {
                    releaseTestAllocationRepository.findById(id).ifPresent(alloc -> {
                        if (alloc.getRelease() != null && alloc.getRelease().getId().equals(releaseId)) {
                            toUpdate.add(alloc);
                        }
                    });
                }
            } else {
                Optional<ReleaseTestAllocation> byPk = releaseTestAllocationRepository.findById(id);
                if (byPk.isPresent()) {
                    toUpdate.add(byPk.get());
                } else {
                    // Try to update any allocation having test_case_id == id
                    List<ReleaseTestAllocation> allMatching = releaseTestAllocationRepository.findAll().stream()
                            .filter(a -> a.getTestCase() != null && a.getTestCase().getId().equals(id))
                            .collect(Collectors.toList());
                    toUpdate.addAll(allMatching);
                }
            }

            for (ReleaseTestAllocation alloc : toUpdate) {
                alloc.setAssignedQa(qa);
                alloc.setStatus("ASSIGNED");
                releaseTestAllocationRepository.save(alloc);
                updatedCount++;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("statusCode", 200);
        response.put("message", "Test cases successfully assigned to QA member.");
        response.put("ownerId", qaId);
        response.put("updatedCount", updatedCount);
        return response;
    }

    @Override
    public ReleaseTestAllocationResponseDto updateExecutionStatus(Long releaseId, Long testCaseId, String status) {
        if (releaseId == null || testCaseId == null) {
            throw new BadRequestException("Release ID and Test Case ID must not be null");
        }

        ReleaseTestAllocation allocation = releaseTestAllocationRepository.findByReleaseIdAndTestCaseId(releaseId, testCaseId)
                .orElseGet(() -> releaseTestAllocationRepository.findById(testCaseId)
                        .orElseThrow(() -> new ResourceNotFoundException("Test allocation not found for release " + releaseId + " and testCase " + testCaseId)));

        String formatted = status != null ? status.trim().toUpperCase() : "NOT_RUN";
        if ("PASSED".equalsIgnoreCase(formatted)) formatted = "PASS";
        if ("FAILED".equalsIgnoreCase(formatted)) formatted = "FAIL";

        allocation.setExecutionStatus(formatted);
        ReleaseTestAllocation saved = releaseTestAllocationRepository.save(allocation);

        if (saved.getTestCase() != null) {
            TestCase tc = saved.getTestCase();
            tc.setExecutionStatus(formatted);
            testCaseRepository.save(tc);
        }

        com.sgic.Defect_Tracker_BE.entity.Defect defect = null;
        if (saved.getTestCase() != null) {
            defect = defectRepository.findTopByReleaseIdAndTestCaseIdOrderByIdDesc(releaseId, saved.getTestCase().getId())
                    .or(() -> defectRepository.findTopByTestCaseIdOrderByIdDesc(saved.getTestCase().getId()))
                    .orElse(null);
        }

        return ReleaseTestAllocationMapper.toResponseDto(saved, defect);
    }
}
