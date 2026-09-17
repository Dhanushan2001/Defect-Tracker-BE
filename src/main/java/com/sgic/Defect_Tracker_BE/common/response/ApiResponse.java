package com.sgic.Defect_Tracker_BE.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private int statusCode;
    private String message;
    private String statusMessage;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(200)
                .message("Success")
                .statusMessage("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(200)
                .message(message)
                .statusMessage(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .statusCode(201)
                .message(message)
                .statusMessage(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .statusCode(statusCode)
                .message(message)
                .statusMessage(message)
                .data(null)
                .build();
    }
}

