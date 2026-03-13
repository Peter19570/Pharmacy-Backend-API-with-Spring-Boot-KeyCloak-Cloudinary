package com.example.pharmacy.apps.products.dto.response;

import java.time.Instant;

public record ProductImageDetailsResponse(
        String url,
        String publicId,
        Instant createdAt
) {
}
