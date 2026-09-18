package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.DefectDto;
import com.sgic.Defect_Tracker_BE.entity.Defect;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefectMapper {

    public DefectDto toDto(Defect defect) {
        if (defect == null) {
            return null;
        }

        String assignedToName = null;
        if (defect.getAssignedTo() != null) {
            String first = defect.getAssignedTo().getFirstName() != null ? defect.getAssignedTo().getFirstName() : "";
            String last = defect.getAssignedTo().getLastName() != null ? defect.getAssignedTo().getLastName() : "";
            assignedToName = (first + " " + last).trim();
            if (assignedToName.isEmpty()) {
                assignedToName = "Employee " + defect.getAssignedTo().getId();
            }
        }

        String reportedByName = null;
        if (defect.getReportedBy() != null) {
            String first = defect.getReportedBy().getFirstName() != null ? defect.getReportedBy().getFirstName() : "";
            String last = defect.getReportedBy().getLastName() != null ? defect.getReportedBy().getLastName() : "";
            reportedByName = (first + " " + last).trim();
            if (reportedByName.isEmpty()) {
                reportedByName = "Employee " + defect.getReportedBy().getId();
            }
        }

        return DefectDto.builder()
                .id(defect.getId())
                .defectNo(defect.getDefectNo())
                .defectId(defect.getDefectNo())
                .title(defect.getTitle())
                .description(defect.getDescription())
                .steps(defect.getSteps())
                .attachment(defect.getAttachment())
                .projectId(defect.getProject() != null ? defect.getProject().getId() : null)
                .projectName(defect.getProject() != null ? defect.getProject().getName() : null)
                .moduleId(defect.getModule() != null ? defect.getModule().getId() : null)
                .moduleName(defect.getModule() != null ? defect.getModule().getName() : null)
                .submoduleId(defect.getSubModule() != null ? defect.getSubModule().getId() : null)
                .subModuleId(defect.getSubModule() != null ? defect.getSubModule().getId() : null)
                .subModuleName(defect.getSubModule() != null ? defect.getSubModule().getName() : null)
                .releaseId(defect.getRelease() != null ? defect.getRelease().getId() : null)
                .releaseName(defect.getRelease() != null ? defect.getRelease().getName() : null)
                .testCaseId(defect.getTestCase() != null ? defect.getTestCase().getId() : null)
                .testCaseNo(defect.getTestCase() != null ? defect.getTestCase().getTestcaseNo() : null)
                .assignedTo(defect.getAssignedTo() != null ? defect.getAssignedTo().getId() : null)
                .assignedToId(defect.getAssignedTo() != null ? defect.getAssignedTo().getId() : null)
                .assignedToName(assignedToName)
                .executerDefect(assignedToName)
                .reportedBy(defect.getReportedBy() != null ? defect.getReportedBy().getId() : null)
                .reportedById(defect.getReportedBy() != null ? defect.getReportedBy().getId() : null)
                .reportedByName(reportedByName)
                .severityId(defect.getSeverity() != null ? defect.getSeverity().getId() : null)
                .severityName(defect.getSeverity() != null ? defect.getSeverity().getName() : null)
                .severity(defect.getSeverity() != null ? defect.getSeverity().getName() : null)
                .priorityId(defect.getPriority() != null ? defect.getPriority().getId() : null)
                .priorityName(defect.getPriority() != null ? defect.getPriority().getName() : null)
                .priority(defect.getPriority() != null ? defect.getPriority().getName() : null)
                .defectTypeId(defect.getDefectType() != null ? defect.getDefectType().getId() : null)
                .defectTypeName(defect.getDefectType() != null ? defect.getDefectType().getDefectTypeName() : null)
                .type(defect.getDefectType() != null ? defect.getDefectType().getDefectTypeName() : null)
                .statusId(defect.getStatus() != null ? defect.getStatus().getId() : null)
                .statusName(defect.getStatus() != null ? defect.getStatus().getName() : null)
                .status(defect.getStatus() != null ? defect.getStatus().getName() : null)
                .createdAt(defect.getCreatedAt())
                .updatedAt(defect.getUpdatedAt())
                .build();
    }

    public DefectDto toDto(Defect defect, Long commentCount) {
        DefectDto dto = toDto(defect);
        if (dto != null) {
            long count = commentCount != null ? commentCount : 0L;
            dto.setCommentCount(count);
            dto.setCommentsCount(count);
        }
        return dto;
    }

    public List<DefectDto> toDtoList(List<Defect> defects) {
        if (defects == null) {
            return Collections.emptyList();
        }
        return defects.stream().map(this::toDto).collect(Collectors.toList());
    }
}
