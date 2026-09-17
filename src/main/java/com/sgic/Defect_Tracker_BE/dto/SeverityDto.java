package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeverityDto {
    private Long id;
    private String name;
    private String severityName;
    private String color;
    private Integer weight;
    private String description;
}
