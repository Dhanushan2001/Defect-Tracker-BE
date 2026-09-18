package com.sgic.Defect_Tracker_BE.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkAssignQaRequest {

    private Long ownerId;
    private Long qaId;
    private Long fromEmployeeId;
    private Long releaseId;

    @Builder.Default
    private List<Long> testCaseIds = new ArrayList<>();

    @Builder.Default
    private List<Long> releaseTestCaseIds = new ArrayList<>();

    public Long resolveQaId() {
        return this.qaId != null ? this.qaId : this.ownerId;
    }

    public List<Long> resolveTestCaseIds() {
        if (this.testCaseIds != null && !this.testCaseIds.isEmpty()) {
            return this.testCaseIds;
        }
        if (this.releaseTestCaseIds != null && !this.releaseTestCaseIds.isEmpty()) {
            return this.releaseTestCaseIds;
        }
        return new ArrayList<>();
    }
}
