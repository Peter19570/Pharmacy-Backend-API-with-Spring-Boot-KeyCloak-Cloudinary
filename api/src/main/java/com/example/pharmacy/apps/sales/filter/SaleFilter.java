package com.example.pharmacy.apps.sales.filter;

import com.example.pharmacy.apps.sales.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class SaleFilter {
    private String transactionCode;
    private String paymentMethod;
    private Instant createdBefore;
    private Instant createdAfter;
}
