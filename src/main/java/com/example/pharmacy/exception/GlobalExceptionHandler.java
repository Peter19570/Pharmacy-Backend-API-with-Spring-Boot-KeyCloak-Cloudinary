package com.example.pharmacy.exception;

import com.example.pharmacy.apps.common.dto.response.ApiResponse;
import com.example.pharmacy.exception.custom.CloudinaryException;
import com.example.pharmacy.exception.custom.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundError(NotFoundException error){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("Not found", error.getMessage()));
    }

    @ExceptionHandler(CloudinaryException.class)
    public ResponseEntity<ApiResponse<String>> handleCloudError(CloudinaryException error){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Cloudinary task failed", error.getMessage()));
    }
}
