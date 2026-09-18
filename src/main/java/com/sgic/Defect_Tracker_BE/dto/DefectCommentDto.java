package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefectCommentDto {

    private Long id;
    private Long defectId;
    private Long userId;
    private Long createdBy;
    private String userName;
    private String createdByName;
    private String comment;
    private String attachment;
    private LocalDateTime createdAt;
    private String createdTime;
}
