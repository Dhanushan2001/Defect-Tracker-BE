package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.ReleaseTestAllocationResponseDto;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.entity.ReleaseTestAllocation;
import com.sgic.Defect_Tracker_BE.entity.TestCase;

public class ReleaseTestAllocationMapper {

    public static ReleaseTestAllocationResponseDto toResponseDto(ReleaseTestAllocation allocation) {
        if (allocation == null) {
            return null;
        }

        TestCase tc = allocation.getTestCase();
        Employee qa = allocation.getAssignedQa();

        String qaName = null;
        if (qa != null) {
            String first = qa.getFirstName() != null ? qa.getFirstName().trim() : "";
            String last = qa.getLastName() != null ? qa.getLastName().trim() : "";
            qaName = (first + " " + last).trim();
            if (qaName.isEmpty()) {
                qaName = "Employee " + qa.getId();
            }
        }

        String formattedNo = null;
        if (tc != null) {
            formattedNo = tc.getTestcaseNo();
            if (formattedNo == null || formattedNo.trim().isEmpty()) {
                formattedNo = String.format("TC-%03d", tc.getId());
            }
        }

        String severityName = (tc != null && tc.getSeverity() != null) ? tc.getSeverity().getName() : null;
        String defectTypeName = (tc != null && tc.getDefectType() != null) ? tc.getDefectType().getDefectTypeName() : null;
        String executionStatus = allocation.getExecutionStatus();
        if (executionStatus == null || executionStatus.trim().isEmpty()) {
            executionStatus = (tc != null && tc.getExecutionStatus() != null) ? tc.getExecutionStatus() : "NOT_RUN";
        }

        String projectName = allocation.getProject() != null ? allocation.getProject().getName() : null;

        return ReleaseTestAllocationResponseDto.builder()
                .id(allocation.getId())
                .testcaseId(tc != null ? tc.getId() : null)
                .testCaseId(formattedNo)
                .testCaseNo(formattedNo)
                .name(tc != null ? tc.getDescription() : null)
                .description(tc != null ? tc.getDescription() : null)
                .steps(tc != null ? tc.getDetailsSteps() : null)
                .detailsSteps(tc != null ? tc.getDetailsSteps() : null)
                .expectedResult(tc != null ? tc.getExpectedResult() : null)
                .type(defectTypeName)
                .defectTypeName(defectTypeName)
                .severity(severityName)
                .severityName(severityName)
                .projectId(allocation.getProject() != null ? allocation.getProject().getId() : null)
                .projectName(projectName)
                .moduleId(allocation.getModule() != null ? allocation.getModule().getId() : null)
                .moduleName(allocation.getModule() != null ? allocation.getModule().getName() : null)
                .submoduleId(allocation.getSubModule() != null ? allocation.getSubModule().getId() : null)
                .subModuleId(allocation.getSubModule() != null ? allocation.getSubModule().getId() : null)
                .subModuleName(allocation.getSubModule() != null ? allocation.getSubModule().getName() : null)
                .releaseId(allocation.getRelease() != null ? allocation.getRelease().getId() : null)
                .releaseName(allocation.getRelease() != null ? allocation.getRelease().getName() : null)
                .assignedTo(qa != null ? qa.getId() : null)
                .assignedQaId(qa != null ? qa.getId() : null)
                .assignedQaName(qaName)
                .status(allocation.getStatus())
                .executionStatus(executionStatus)
                .createdAt(allocation.getCreatedAt())
                .updatedAt(allocation.getUpdatedAt())
                .build();
    }

    public static ReleaseTestAllocationResponseDto toResponseDto(ReleaseTestAllocation allocation, com.sgic.Defect_Tracker_BE.entity.Defect defect) {
        ReleaseTestAllocationResponseDto dto = toResponseDto(allocation);
        if (dto != null && defect != null) {
            dto.setDefectNo(defect.getDefectNo());
            dto.setDefectId(defect.getId());
            if (defect.getAssignedTo() != null) {
                dto.setAssignedDevId(defect.getAssignedTo().getId());
                String first = defect.getAssignedTo().getFirstName() != null ? defect.getAssignedTo().getFirstName().trim() : "";
                String last = defect.getAssignedTo().getLastName() != null ? defect.getAssignedTo().getLastName().trim() : "";
                String devName = (first + " " + last).trim();
                dto.setAssignedDev(devName.isEmpty() ? "Employee " + defect.getAssignedTo().getId() : devName);
                dto.setAssignedDevName(dto.getAssignedDev());
            }
        }
        return dto;
    }
}
