package com.sgic.Defect_Tracker_BE.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkAllocateReleaseTestCasesRequest {

    private Long releaseId;
    @Builder.Default
    private List<Long> testCaseIds = new ArrayList<>();
}
