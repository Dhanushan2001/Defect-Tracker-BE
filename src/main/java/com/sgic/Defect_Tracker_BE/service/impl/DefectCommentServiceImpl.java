package com.sgic.Defect_Tracker_BE.service.impl;

import com.sgic.Defect_Tracker_BE.common.exception.BadRequestException;
import com.sgic.Defect_Tracker_BE.common.exception.ResourceNotFoundException;
import com.sgic.Defect_Tracker_BE.dto.CreateCommentRequest;
import com.sgic.Defect_Tracker_BE.dto.DefectCommentDto;
import com.sgic.Defect_Tracker_BE.entity.Defect;
import com.sgic.Defect_Tracker_BE.entity.DefectComment;
import com.sgic.Defect_Tracker_BE.entity.Employee;
import com.sgic.Defect_Tracker_BE.repository.DefectCommentRepository;
import com.sgic.Defect_Tracker_BE.repository.DefectRepository;
import com.sgic.Defect_Tracker_BE.repository.EmployeeRepository;
import com.sgic.Defect_Tracker_BE.service.DefectCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DefectCommentServiceImpl implements DefectCommentService {

    private final DefectCommentRepository defectCommentRepository;
    private final DefectRepository defectRepository;
    private final EmployeeRepository employeeRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public DefectCommentDto createComment(CreateCommentRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null");
        }

        Long defectId = request.resolveDefectId();
        if (defectId == null) {
            throw new BadRequestException("Defect ID is required");
        }

        Defect defect = defectRepository.findById(defectId)
                .orElseThrow(() -> new ResourceNotFoundException("Defect not found with id: " + defectId));

        if (request.getComment() == null || request.getComment().trim().isEmpty()) {
            throw new BadRequestException("Comment text cannot be empty");
        }

        Employee user = null;
        Long userId = request.resolveUserId();
        if (userId != null) {
            user = employeeRepository.findById(userId).orElse(null);
        }

        String userName = request.getUserName();
        if (user != null) {
            String first = user.getFirstName() != null ? user.getFirstName() : "";
            String last = user.getLastName() != null ? user.getLastName() : "";
            String fullName = (first + " " + last).trim();
            if (!fullName.isEmpty()) {
                userName = fullName;
            }
        }
        if (userName == null || userName.trim().isEmpty()) {
            userName = user != null ? "User " + user.getId() : "QA Tester";
        }

        DefectComment comment = DefectComment.builder()
                .defect(defect)
                .user(user)
                .authorId(userId)
                .userName(userName)
                .comment(request.getComment().trim())
                .attachment(request.getAttachment())
                .build();

        DefectComment saved = defectCommentRepository.save(comment);

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DefectCommentDto> getCommentsByDefectId(Long defectId) {
        if (defectId == null) {
            return Collections.emptyList();
        }
        List<DefectComment> comments = defectCommentRepository.findByDefectIdOrderByIdAsc(defectId);
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getCommentCount(Long defectId) {
        if (defectId == null) {
            return 0;
        }
        return defectCommentRepository.countByDefectId(defectId);
    }

    private DefectCommentDto toDto(DefectComment comment) {
        if (comment == null) return null;

        Long userId = comment.getUser() != null ? comment.getUser().getId() : comment.getAuthorId();
        String timeStr = comment.getCreatedAt() != null ? comment.getCreatedAt().format(TIME_FORMATTER) : null;

        return DefectCommentDto.builder()
                .id(comment.getId())
                .defectId(comment.getDefect() != null ? comment.getDefect().getId() : null)
                .userId(userId)
                .createdBy(userId)
                .userName(comment.getUserName())
                .createdByName(comment.getUserName())
                .comment(comment.getComment())
                .attachment(comment.getAttachment())
                .createdAt(comment.getCreatedAt())
                .createdTime(timeStr)
                .build();
    }
}
