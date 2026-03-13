package com.example.pharmacy.apps.common.dto.response;

public record ApiResponse<T>(
        String msg,
        T data
) {
}
