package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.*;
import com.sgic.Defect_Tracker_BE.entity.StatusType;
import com.sgic.Defect_Tracker_BE.entity.StatusWorkflow;
import com.sgic.Defect_Tracker_BE.repository.StatusTypeRepository;
import com.sgic.Defect_Tracker_BE.repository.StatusWorkflowRepository;
import com.sgic.Defect_Tracker_BE.service.StatusWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StatusWorkflowServiceImpl implements StatusWorkflowService {

    private final StatusWorkflowRepository statusWorkflowRepository;
    private final StatusTypeRepository statusTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowTransitionDto> getAllWorkflows() {
        List<StatusWorkflow> workflows = statusWorkflowRepository.findAll();
        return workflows.stream()
                .map(this::mapToTransitionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowTransitionDto> saveWorkflow(SaveWorkflowRequest request) {
        if (request == null) {
            throw new BadRequestException("Workflow request cannot be null");
        }

        // 1. Update status node canvas positions
        if (request.getNodes() != null) {
            for (WorkflowNodeDto node : request.getNodes()) {
                if (node.getId() != null) {
                    Optional<StatusType> statusOpt = statusTypeRepository.findById(node.getId());
                    if (statusOpt.isPresent()) {
                        StatusType statusType = statusOpt.get();
                        statusType.setPositionX(node.getPositionX());
                        statusType.setPositionY(node.getPositionY());
                        statusTypeRepository.save(statusType);
                    }
                }
            }
        }

        // 2. Replace or recreate workflow connections
        if (request.getConnections() != null) {
            statusWorkflowRepository.deleteAllInBatch();

            Set<String> seenPairs = new HashSet<>();
            List<StatusWorkflow> newWorkflows = new ArrayList<>();

            for (WorkflowConnectionDto conn : request.getConnections()) {
                if (conn.getFromStatusId() == null || conn.getToStatusId() == null) {
                    continue;
                }

                String pairKey = conn.getFromStatusId() + "->" + conn.getToStatusId();
                if (seenPairs.contains(pairKey)) {
                    continue;
                }
                seenPairs.add(pairKey);

                StatusType fromStatus = statusTypeRepository.findById(conn.getFromStatusId())
                        .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + conn.getFromStatusId()));
                StatusType toStatus = statusTypeRepository.findById(conn.getToStatusId())
                        .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + conn.getToStatusId()));

                StatusWorkflow workflow = StatusWorkflow.builder()
                        .fromStatus(fromStatus)
                        .toStatus(toStatus)
                        .build();

                newWorkflows.add(workflow);
            }

            statusWorkflowRepository.saveAll(newWorkflows);
        }

        return getAllWorkflows();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowStatusDto> getNextStatuses(Long fromStatusId) {
        if (fromStatusId == null) {
            throw new BadRequestException("Status ID cannot be null");
        }

        List<StatusWorkflow> transitions = statusWorkflowRepository.findByFromStatusId(fromStatusId);
        return transitions.stream()
                .map(sw -> mapToStatusDto(sw.getToStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWorkflowTransition(Long id) {
        if (!statusWorkflowRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workflow transition not found with id: " + id);
        }
        statusWorkflowRepository.deleteById(id);
    }

    private WorkflowTransitionDto mapToTransitionDto(StatusWorkflow workflow) {
        return WorkflowTransitionDto.builder()
                .id(workflow.getId())
                .fromStatus(mapToStatusDto(workflow.getFromStatus()))
                .toStatus(mapToStatusDto(workflow.getToStatus()))
                .build();
    }

    private WorkflowStatusDto mapToStatusDto(StatusType status) {
        if (status == null) {
            return null;
        }
        return WorkflowStatusDto.builder()
                .id(status.getId())
                .name(status.getName())
                .statusName(status.getName())
                .color(status.getColor())
                .type(status.getType())
                .statusType(status.getType())
                .positionX(status.getPositionX())
                .positionY(status.getPositionY())
                .build();
    }
}
