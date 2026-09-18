package com.sgic.Defect_Tracker_BE.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManyToManyAllocationRequest {

    @Builder.Default
    private List<Long> releaseIds = new ArrayList<>();

    @Builder.Default
    private List<Long> testCaseIds = new ArrayList<>();
}
