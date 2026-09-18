package com.sgic.Defect_Tracker_BE.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {

    private Long defectId;

    @JsonProperty("defect_id")
    private Long defectIdAlias;

    private Long userId;

    @JsonProperty("user_id")
    private Long userIdAlias;

    @JsonProperty("createdBy")
    private Long createdByAlias;

    private String userName;

    private String comment;

    private String attachment;

    public Long resolveDefectId() {
        return defectId != null ? defectId : defectIdAlias;
    }

    public Long resolveUserId() {
        if (userId != null) return userId;
        if (userIdAlias != null) return userIdAlias;
        if (createdByAlias != null) return createdByAlias;
        return null;
    }
}
