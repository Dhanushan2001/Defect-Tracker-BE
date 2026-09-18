package com.sgic.Defect_Tracker_BE.service;

import com.sgic.Defect_Tracker_BE.dto.CreateCommentRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectCommentDto;

import java.util.List;

public interface DefectCommentService {

    DefectCommentDto createComment(CreateCommentRequest request);

    List<DefectCommentDto> getCommentsByDefectId(Long defectId);

    long getCommentCount(Long defectId);
}
