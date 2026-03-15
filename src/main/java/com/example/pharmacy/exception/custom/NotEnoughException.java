package com.example.pharmacy.exception.custom;

public class NotEnoughException extends RuntimeException {
    public NotEnoughException(String msg) {
        super(msg);
    }
}
