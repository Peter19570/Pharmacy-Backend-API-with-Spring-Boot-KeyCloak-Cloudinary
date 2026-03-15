package com.example.pharmacy.apps.saleItems.dto.response;

import java.math.BigDecimal;

public record SaleItemResponse(
        Integer quantitySold,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
