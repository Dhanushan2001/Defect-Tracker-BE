package com.sgic.Defect_Tracker_BE.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedData<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private int size;
    private int number;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;
}
