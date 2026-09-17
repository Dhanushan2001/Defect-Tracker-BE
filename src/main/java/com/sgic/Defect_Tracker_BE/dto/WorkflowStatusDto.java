package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStatusDto {
    private Long id;
    private String name;
    private String statusName;
    private String color;
    private String type;
    private String statusType;
    private Double positionX;
    private Double positionY;
}
