package com.example.pharmacy.apps.products.dto.response;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String category
) {
}
