package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.CreateDefectRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectDto;
import com.sgic.Defect_Tracker_BE.dto.DefectHistoryDto;
import com.sgic.Defect_Tracker_BE.entity.*;
import com.sgic.Defect_Tracker_BE.entity.Module;
import com.sgic.Defect_Tracker_BE.mapper.DefectMapper;
import com.sgic.Defect_Tracker_BE.repository.*;
import com.sgic.Defect_Tracker_BE.service.DefectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DefectServiceImpl implements DefectService {

    private final DefectRepository defectRepository;
    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final SubModuleRepository subModuleRepository;
    private final ReleaseRepository releaseRepository;
    private final TestCaseRepository testCaseRepository;
    private final EmployeeRepository employeeRepository;
    private final SeverityRepository severityRepository;
    private final PriorityRepository priorityRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final StatusTypeRepository statusTypeRepository;
    private final ReleaseTestAllocationRepository releaseTestAllocationRepository;
    private final DefectHistoryRepository defectHistoryRepository;
    private final DefectCommentRepository defectCommentRepository;
    private final DefectMapper defectMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public DefectDto createDefect(CreateDefectRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        TestCase testCase = null;
        Long testCaseId = request.resolveTestCaseId();
        if (testCaseId != null) {
            testCase = testCaseRepository.findById(testCaseId).orElse(null);
        }

        // Release (optional)
        Release release = null;
        Long releaseId = request.resolveReleaseId();
        if (releaseId != null) {
            release = releaseRepository.findById(releaseId).orElse(null);
        }

        // SubModule (optional / resolved)
        SubModule subModule = null;
        Long rawSubModuleId = request.resolveSubModuleId();
        if (rawSubModuleId != null) {
            subModule = subModuleRepository.findById(rawSubModuleId).orElse(null);
        } else if (testCase != null && testCase.getSubModule() != null) {
            subModule = testCase.getSubModule();
        }

        // Module
        Module module = null;
        Long rawModuleId = request.resolveModuleId();
        if (rawModuleId != null) {
            module = moduleRepository.findById(rawModuleId).orElse(null);
        } else if (subModule != null && subModule.getModule() != null) {
            module = subModule.getModule();
        } else if (testCase != null && testCase.getModule() != null) {
            module = testCase.getModule();
        }

        if (module == null) {
            module = moduleRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new BadRequestException("Module is required"));
        }

        if (subModule == null) {
            subModule = subModuleRepository.findByModuleId(module.getId()).stream().findFirst().orElse(null);
        }

        // Project
        Project project = null;
        Long rawProjectId = request.resolveProjectId();
        if (rawProjectId != null) {
            project = projectRepository.findById(rawProjectId).orElse(null);
        }
        if (project == null && module.getProject() != null) {
            project = module.getProject();
        }
        if (project == null && subModule != null && subModule.getProject() != null) {
            project = subModule.getProject();
        }
        if (project == null && release != null && release.getProject() != null) {
            project = release.getProject();
        }
        if (project == null && testCase != null && testCase.getProject() != null) {
            project = testCase.getProject();
        }
        if (project == null) {
            project = projectRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new BadRequestException("Project is required"));
        }

        // Description and Title - strictly prioritize actual description entered
        String description = request.getDescription() != null && !request.getDescription().trim().isEmpty()
                ? request.getDescription().trim()
                : null;

        String title = request.getTitle() != null && !request.getTitle().trim().isEmpty()
                ? request.getTitle().trim()
                : null;

        if (title == null) {
            if (description != null) {
                title = description;
            } else if (testCase != null && testCase.getDescription() != null) {
                title = testCase.getDescription();
            } else {
                title = "Defect for " + module.getName() + " / " + subModule.getName();
            }
        }

        if (description == null) {
            description = title;
        }

        String steps = request.getSteps();
        if (steps == null && testCase != null) {
            steps = testCase.getDetailsSteps();
        }

        // Assigned To (Developer)
        Employee assignedTo = null;
        Long assignedToId = request.resolveAssignedTo();
        if (assignedToId != null) {
            assignedTo = employeeRepository.findById(assignedToId).orElse(null);
        }

        // Reported By
        Employee reportedBy = null;
        Long reportedById = request.resolveReportedBy();
        if (reportedById != null) {
            reportedBy = employeeRepository.findById(reportedById).orElse(null);
        }

        // Severity
        Severity severity = null;
        if (request.getSeverityId() != null) {
            severity = severityRepository.findById(request.getSeverityId()).orElse(null);
        } else if (request.getSeverity() != null && !request.getSeverity().trim().isEmpty()) {
            severity = severityRepository.findByNameIgnoreCase(request.getSeverity().trim()).orElse(null);
        }
        if (severity == null && testCase != null) {
            severity = testCase.getSeverity();
        }

        // Priority
        Priority priority = null;
        if (request.getPriorityId() != null) {
            priority = priorityRepository.findById(request.getPriorityId()).orElse(null);
        } else if (request.getPriority() != null && !request.getPriority().trim().isEmpty()) {
            priority = priorityRepository.findByNameIgnoreCase(request.getPriority().trim()).orElse(null);
        }
        if (priority == null) {
            priority = priorityRepository.findByNameIgnoreCase("Medium")
                    .or(() -> priorityRepository.findAll().stream().findFirst())
                    .orElse(null);
        }

        // Defect Type
        DefectType defectType = null;
        Long typeId = request.resolveDefectTypeId();
        if (typeId != null) {
            defectType = defectTypeRepository.findById(typeId).orElse(null);
        } else if (request.getType() != null && !request.getType().trim().isEmpty()) {
            defectType = defectTypeRepository.findByDefectTypeNameIgnoreCase(request.getType().trim()).orElse(null);
        }
        if (defectType == null && testCase != null) {
            defectType = testCase.getDefectType();
        }
        if (defectType == null) {
            defectType = defectTypeRepository.findAll().stream().findFirst().orElse(null);
        }

        // Status
        StatusType status = null;
        Long statusId = request.resolveStatusId();
        if (statusId != null) {
            status = statusTypeRepository.findById(statusId).orElse(null);
        } else if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            status = statusTypeRepository.findByNameIgnoreCase(request.getStatus().trim()).orElse(null);
        }
        if (status == null) {
            status = statusTypeRepository.findByNameIgnoreCase("New")
                    .or(() -> statusTypeRepository.findByNameIgnoreCase("Open"))
                    .or(() -> statusTypeRepository.findAll().stream().findFirst())
                    .orElse(null);
        }

        // Sequential Defect Number
        Long nextId = defectRepository.findTopByOrderByIdDesc().map(d -> d.getId() + 1).orElse(1L);
        String defectNo = String.format("DEF-%03d", nextId);

        Defect defect = Defect.builder()
                .defectNo(defectNo)
                .title(title.trim())
                .description(description.trim())
                .steps(steps)
                .project(project)
                .module(module)
                .subModule(subModule)
                .release(release)
                .testCase(testCase)
                .assignedTo(assignedTo)
                .reportedBy(reportedBy)
                .severity(severity)
                .priority(priority)
                .defectType(defectType)
                .status(status)
                .attachment(request.getAttachment())
                .build();

        Defect saved = defectRepository.save(defect);

        // Update testcase execution status to FAIL
        if (testCase != null) {
            testCase.setExecutionStatus("FAIL");
            testCaseRepository.save(testCase);

            if (release != null) {
                releaseTestAllocationRepository.findByReleaseIdAndTestCaseId(release.getId(), testCase.getId())
                        .ifPresent(rta -> {
                            rta.setExecutionStatus("FAIL");
                            releaseTestAllocationRepository.save(rta);
                        });
            }
        }

        // Record initial history
        String assignedByName = null;
        if (reportedBy != null) {
            assignedByName = (reportedBy.getFirstName() + " " + (reportedBy.getLastName() != null ? reportedBy.getLastName() : "")).trim();
        }
        if (assignedByName == null || assignedByName.isEmpty()) {
            assignedByName = "QA Tester";
        }

        String assignedToName = null;
        if (assignedTo != null) {
            assignedToName = (assignedTo.getFirstName() + " " + (assignedTo.getLastName() != null ? assignedTo.getLastName() : "")).trim();
        }
        if (assignedToName == null || assignedToName.isEmpty()) {
            assignedToName = "-";
        }

        String initialStatusName = status != null ? status.getName() : "New";

        DefectHistory history = DefectHistory.builder()
                .defect(saved)
                .assignedByName(assignedByName)
                .assignedToName(assignedToName)
                .previousStatus("New")
                .defectStatus(initialStatusName)
                .name("Defect created")
                .createdBy(assignedByName)
                .updatedBy(assignedByName)
                .createdAt(LocalDateTime.now())
                .build();
        defectHistoryRepository.save(history);

        return defectMapper.toDto(saved, 0L);
    }

    @Override
    public DefectDto updateDefect(Long id, CreateDefectRequest request) {
        if (id == null) {
            throw new BadRequestException("Defect ID cannot be null");
        }
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect not found with id: " + id));

        String previousStatus = defect.getStatus() != null ? defect.getStatus().getName() : "New";
        String previousAssignee = defect.getAssignedTo() != null
                ? (defect.getAssignedTo().getFirstName() + " " + (defect.getAssignedTo().getLastName() != null ? defect.getAssignedTo().getLastName() : "")).trim()
                : "-";

        // Description & Title
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            defect.setDescription(request.getDescription().trim());
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                defect.setTitle(request.getDescription().trim());
            }
        }
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            defect.setTitle(request.getTitle().trim());
        }

        // Steps
        if (request.getSteps() != null) {
            defect.setSteps(request.getSteps());
        }

        // Module
        Long moduleId = request.resolveModuleId();
        if (moduleId != null) {
            moduleRepository.findById(moduleId).ifPresent(defect::setModule);
        }

        // SubModule
        Long subModuleId = request.resolveSubModuleId();
        if (subModuleId != null) {
            subModuleRepository.findById(subModuleId).ifPresent(defect::setSubModule);
        }

        // Release
        Long releaseId = request.resolveReleaseId();
        if (releaseId != null) {
            releaseRepository.findById(releaseId).ifPresent(defect::setRelease);
        }

        // Severity
        if (request.getSeverityId() != null) {
            severityRepository.findById(request.getSeverityId()).ifPresent(defect::setSeverity);
        } else if (request.getSeverity() != null && !request.getSeverity().trim().isEmpty()) {
            severityRepository.findByNameIgnoreCase(request.getSeverity().trim()).ifPresent(defect::setSeverity);
        }

        // Priority
        if (request.getPriorityId() != null) {
            priorityRepository.findById(request.getPriorityId()).ifPresent(defect::setPriority);
        } else if (request.getPriority() != null && !request.getPriority().trim().isEmpty()) {
            priorityRepository.findByNameIgnoreCase(request.getPriority().trim()).ifPresent(defect::setPriority);
        }

        // Defect Type
        Long typeId = request.resolveDefectTypeId();
        if (typeId != null) {
            defectTypeRepository.findById(typeId).ifPresent(defect::setDefectType);
        } else if (request.getType() != null && !request.getType().trim().isEmpty()) {
            defectTypeRepository.findByDefectTypeNameIgnoreCase(request.getType().trim()).ifPresent(defect::setDefectType);
        }

        // Status
        Long statusId = request.resolveStatusId();
        if (statusId != null) {
            statusTypeRepository.findById(statusId).ifPresent(defect::setStatus);
        } else if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            statusTypeRepository.findByNameIgnoreCase(request.getStatus().trim()).ifPresent(defect::setStatus);
        }

        // Assigned To
        Long assignedToId = request.resolveAssignedTo();
        if (assignedToId != null) {
            employeeRepository.findById(assignedToId).ifPresent(defect::setAssignedTo);
        }

        // Attachment
        if (request.getAttachment() != null) {
            defect.setAttachment(request.getAttachment());
        }

        defect.setUpdatedAt(LocalDateTime.now());
        Defect updated = defectRepository.save(defect);

        // Track History
        String newStatus = updated.getStatus() != null ? updated.getStatus().getName() : previousStatus;
        String newAssignee = updated.getAssignedTo() != null
                ? (updated.getAssignedTo().getFirstName() + " " + (updated.getAssignedTo().getLastName() != null ? updated.getAssignedTo().getLastName() : "")).trim()
                : previousAssignee;

        String actionDescription = "Defect updated";
        if (!previousStatus.equalsIgnoreCase(newStatus)) {
            actionDescription = "Status changed from " + previousStatus + " to " + newStatus;
        } else if (!previousAssignee.equalsIgnoreCase(newAssignee)) {
            actionDescription = "Reassigned from " + previousAssignee + " to " + newAssignee;
        }

        String reporterName = updated.getReportedBy() != null
                ? (updated.getReportedBy().getFirstName() + " " + (updated.getReportedBy().getLastName() != null ? updated.getReportedBy().getLastName() : "")).trim()
                : "User";

        DefectHistory history = DefectHistory.builder()
                .defect(updated)
                .assignedByName(reporterName)
                .assignedToName(newAssignee)
                .previousStatus(previousStatus)
                .defectStatus(newStatus)
                .name(actionDescription)
                .createdBy(reporterName)
                .updatedBy(reporterName)
                .createdAt(LocalDateTime.now())
                .build();
        defectHistoryRepository.save(history);

        long commentsCount = defectCommentRepository.countByDefectId(updated.getId());
        return defectMapper.toDto(updated, commentsCount);
    }

    @Override
    @Transactional(readOnly = true)
    public DefectDto getDefectById(Long id) {
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect not found with id: " + id));
        long count = defectCommentRepository.countByDefectId(id);
        return defectMapper.toDto(defect, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DefectDto> getDefectsByProjectId(Long projectId) {
        if (projectId == null) {
            return Collections.emptyList();
        }
        List<Defect> defects = defectRepository.findByProjectIdOrderByIdAsc(projectId);
        return defects.stream().map(d -> {
            long count = defectCommentRepository.countByDefectId(d.getId());
            return defectMapper.toDto(d, count);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DefectDto> getDefectsByReleaseId(Long releaseId) {
        if (releaseId == null) {
            return Collections.emptyList();
        }
        List<Defect> defects = defectRepository.findByReleaseIdOrderByIdDesc(releaseId);
        return defects.stream().map(d -> {
            long count = defectCommentRepository.countByDefectId(d.getId());
            return defectMapper.toDto(d, count);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DefectDto> getAllDefects() {
        List<Defect> defects = defectRepository.findAll();
        return defects.stream().map(d -> {
            long count = defectCommentRepository.countByDefectId(d.getId());
            return defectMapper.toDto(d, count);
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteDefect(Long id) {
        Defect defect = defectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defect not found with id: " + id));
        defectCommentRepository.deleteByDefectId(id);
        defectHistoryRepository.deleteByDefectId(id);
        defectRepository.delete(defect);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DefectHistoryDto> getDefectHistory(Long defectId) {
        if (defectId == null) {
            return Collections.emptyList();
        }
        List<DefectHistory> historyList = defectHistoryRepository.findByDefectIdOrderByIdDesc(defectId);
        return historyList.stream().map(h -> DefectHistoryDto.builder()
                .id(h.getId())
                .defectId(h.getDefect() != null ? h.getDefect().getId() : defectId)
                .assignedByName(h.getAssignedByName() != null ? h.getAssignedByName() : "-")
                .assignedToName(h.getAssignedToName() != null ? h.getAssignedToName() : "-")
                .previousStatus(h.getPreviousStatus() != null ? h.getPreviousStatus() : "-")
                .defectStatus(h.getDefectStatus() != null ? h.getDefectStatus() : "-")
                .name(h.getName() != null ? h.getName() : "-")
                .defectDate(h.getCreatedAt() != null ? h.getCreatedAt().format(DATE_FORMATTER) : "")
                .defectTime(h.getCreatedAt() != null ? h.getCreatedAt().format(TIME_FORMATTER) : "")
                .createdBy(h.getCreatedBy() != null ? h.getCreatedBy() : "-")
                .updatedBy(h.getUpdatedBy() != null ? h.getUpdatedBy() : "-")
                .createdAt(h.getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }
}
