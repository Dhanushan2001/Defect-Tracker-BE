package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.SaveWorkflowRequest;
import com.sgic.Defect_Tracker_BE.dto.WorkflowStatusDto;
import com.sgic.Defect_Tracker_BE.dto.WorkflowTransitionDto;

import java.util.List;

public interface StatusWorkflowService {

    List<WorkflowTransitionDto> getAllWorkflows();

    List<WorkflowTransitionDto> saveWorkflow(SaveWorkflowRequest request);

    List<WorkflowStatusDto> getNextStatuses(Long fromStatusId);

    void deleteWorkflowTransition(Long id);
}
