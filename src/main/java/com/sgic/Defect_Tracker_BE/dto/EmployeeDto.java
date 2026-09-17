package com.sgic.Defect_Tracker_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String name;
    private String gender;
    private String email;
    private String contactNo;
    private Long designationId;
    private String designationName;
    private String joinDate;
    private Boolean isActive;
    private String status;
    private List<String> skills;
    private Integer experience;
    private Integer availability;
}
