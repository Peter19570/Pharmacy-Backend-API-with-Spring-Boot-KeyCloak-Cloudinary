package com.example.pharmacy.apps.products.filter;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
public class ProductFilter {
    private String name;
    private String category;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Instant startDate;
    private Instant endDate;
    private Boolean expired;
    private Boolean lowStock;
}
