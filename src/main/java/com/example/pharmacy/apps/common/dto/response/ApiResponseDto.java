package com.example.pharmacy.apps.common.dto.response;

public record ApiResponseDto<T>(
        String msg,
        T data
) {
}
