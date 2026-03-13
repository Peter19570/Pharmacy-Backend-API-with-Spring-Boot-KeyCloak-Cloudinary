package com.example.pharmacy.apps.products.dto.response;

import com.example.pharmacy.apps.products.model.ProductImage;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductDetailsResponse(
        UUID id,
        String name,
        String category,
        String sku,
        String description,
        String purchasePrice,
        String sellingPrice,
        Integer quantity,
        Instant expirationDate,
        List<ProductImage> images,
        Instant createdAt

) {
}
