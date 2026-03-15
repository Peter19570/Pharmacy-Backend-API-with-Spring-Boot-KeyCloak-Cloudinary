package com.example.pharmacy.apps.sales.dto.response;

import com.example.pharmacy.apps.saleItems.dto.response.SaleItemResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SaleDetailsResponse(
        UUID id,
        String transactionCode,
        BigDecimal totalAmount,
        BigDecimal amountPaid,
        BigDecimal changeDue,
        String paymentMethod,
        List<SaleItemResponse> saleItems,
        Instant createdAt
) {
}
