package com.example.pharmacy.exception.custom;

public class LowCountException extends RuntimeException {
    public LowCountException(String msg) {
        super(msg);
    }
}
