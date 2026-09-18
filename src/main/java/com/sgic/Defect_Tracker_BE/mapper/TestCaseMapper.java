package com.sgic.Defect_Tracker_BE.mapper;

import com.sgic.Defect_Tracker_BE.dto.TestCaseResponseDto;
import com.sgic.Defect_Tracker_BE.entity.DefectType;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.entity.Project;
import com.sgic.Defect_Tracker_BE.entity.Severity;
import com.sgic.Defect_Tracker_BE.entity.SubModule;
import com.sgic.Defect_Tracker_BE.entity.TestCase;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestCaseMapper {

    public TestCaseResponseDto toDto(TestCase entity) {
        if (entity == null) {
            return null;
        }

        SubModule subModule = entity.getSubModule();
        Module module = entity.getModule();
        Project project = entity.getProject();
        Severity severity = entity.getSeverity();
        DefectType defectType = entity.getDefectType();

        String subModuleName = subModule != null ? subModule.getName() : "";
        String moduleName = module != null ? module.getName() : "";
        String projectName = project != null ? project.getName() : "";
        String severityName = severity != null ? severity.getName() : "";
        String defectTypeName = defectType != null ? defectType.getDefectTypeName() : "";

        String formattedNo = entity.getTestcaseNo();
        if (formattedNo == null || formattedNo.isBlank()) {
            if (entity.getId() != null) {
                formattedNo = String.format("TC-%03d", entity.getId());
            } else {
                formattedNo = "TC-001";
            }
        }

        String createdAtStr = entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null;
        String updatedAtStr = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null;

        return TestCaseResponseDto.builder()
                .id(entity.getId())
                .no(formattedNo)
                .testcaseNo(formattedNo)
                .testCaseId(entity.getId())
                .description(entity.getDescription())
                .detailsSteps(entity.getDetailsSteps())
                .steps(entity.getDetailsSteps())
                .expectedResult(entity.getExpectedResult())
                .subModuleId(subModule != null ? subModule.getId() : null)
                .subModuleName(subModuleName)
                .subModule(subModuleName)
                .moduleId(module != null ? module.getId() : null)
                .moduleName(moduleName)
                .module(moduleName)
                .projectId(project != null ? project.getId() : null)
                .projectName(projectName)
                .severityId(severity != null ? severity.getId() : null)
                .severityName(severityName)
                .severity(severityName)
                .defectTypeId(defectType != null ? defectType.getId() : null)
                .defectTypeName(defectTypeName)
                .type(defectTypeName)
                .executionStatus(entity.getExecutionStatus() != null ? entity.getExecutionStatus() : "NOT_RUN")
                .createdAt(createdAtStr)
                .updatedAt(updatedAtStr)
                .build();
    }

    public List<TestCaseResponseDto> toDtoList(List<TestCase> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}
