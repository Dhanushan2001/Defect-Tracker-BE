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
public class DefectHistoryDto {

    private Long id;
    private Long defectId;
    private String assignedByName;
    private String assignedToName;
    private String previousStatus;
    private String defectStatus;
    private String name;
    private String defectDate;
    private String defectTime;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
}
