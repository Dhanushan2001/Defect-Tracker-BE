package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseTypeDto {
    private Long id;
    private String name;
    private String releaseTypeName;
    private String description;
}
