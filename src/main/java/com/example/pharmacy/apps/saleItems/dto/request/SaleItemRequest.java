package com.example.pharmacy.apps.saleItems.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SaleItemRequest(

        @NotNull
        @Size(min = 0)
        Integer quantitySold,

        @NotNull
        UUID productId
) {
}
