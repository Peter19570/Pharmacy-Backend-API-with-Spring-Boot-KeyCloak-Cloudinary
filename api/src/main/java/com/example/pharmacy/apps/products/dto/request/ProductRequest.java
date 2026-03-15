package com.example.pharmacy.apps.products.dto.request;

import com.example.pharmacy.apps.products.model.ProductImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductRequest(
        @NotNull
        String name,

        @NotNull
        String sku,

        String description,

        @NotNull
        String category,

        @NotNull
        @Min(0)
        BigDecimal purchasePrice,

        @NotNull
        @Min(0)
        BigDecimal sellingPrice,

        @NotNull
        @Min(0)
        Integer quantity,

        @NotNull
//        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Instant expirationDate,

        @NotNull
        @Min(0)
        Integer lowStockThreshold,

        List<ProductImage> images
) {
}
