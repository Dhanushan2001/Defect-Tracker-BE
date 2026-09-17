package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    private String name;
    private String prefix;
    private String projectType;
    private String status;
    private String description;
    private String startDate;
    private String endDate;
    private Long projectManagerId;
    private Integer managerAllocation;
    private String clientName;
    private String clientCountry;
    private String clientState;
    private String clientEmail;
    private String clientPhone;
    private String address;
}
