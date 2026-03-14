package com.example.pharmacy.apps.sales.dto.request;

import com.example.pharmacy.apps.saleItems.dto.request.SaleItemRequest;
import com.example.pharmacy.apps.saleItems.model.SaleItem;
import com.example.pharmacy.apps.sales.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record SaleRequest(
        @NotNull
        BigDecimal amountPaid,

        @NotNull
        PaymentMethod paymentMethod,

        @NotNull
        List<SaleItemRequest> saleItems
) {
}
