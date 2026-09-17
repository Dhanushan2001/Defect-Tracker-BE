package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusTypeDto {
    private Long id;
    private String name;
    private String statusName;
    private String defectStatusName;
    private String color;
    private String colorCode;
    private String type;
    private String statusType;
    private String description;
    private Double positionX;
    private Double positionY;
}
