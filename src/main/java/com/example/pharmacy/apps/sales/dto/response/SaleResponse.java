package com.example.pharmacy.apps.sales.dto.response;

import com.example.pharmacy.apps.saleItems.dto.response.SaleItemResponse;
import com.example.pharmacy.apps.saleItems.model.SaleItem;
import com.example.pharmacy.apps.sales.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SaleResponse(
        String transactionCode,
        BigDecimal totalAmount,
        BigDecimal amountPaid,
        BigDecimal changeDue,
        String paymentMethod
) {
}
