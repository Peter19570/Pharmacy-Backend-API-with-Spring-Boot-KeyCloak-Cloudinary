package com.example.pharmacy.apps.common.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TransCodeGenerator {
    public static String generateTransactionCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "TXN-" + datePart + "-" + randomPart;
    }
}
