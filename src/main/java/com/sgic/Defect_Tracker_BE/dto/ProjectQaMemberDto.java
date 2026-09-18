package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectQaMemberDto {

    private Long id;
    private Long userId;
    private String name;
    private String userFullName;
    private String roleName;
    private String designationName;
}
