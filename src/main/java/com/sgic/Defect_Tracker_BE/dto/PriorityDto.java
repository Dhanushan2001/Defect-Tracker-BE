package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDto {
    private Long id;
    private String name;
    private String priorityName;
    private String color;
    private Integer weight;
    private String description;
}
