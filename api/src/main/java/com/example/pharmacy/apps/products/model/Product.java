package com.example.pharmacy.apps.products.model;

import com.example.pharmacy.apps.common.model.BaseEntity;
import com.example.pharmacy.apps.products.enums.ProductCategory;
import com.example.pharmacy.apps.saleItems.model.SaleItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sku;

    @Column(length = 350)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category = ProductCategory.OTC;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Instant expirationDate;

    @Column(nullable = false)
    private Integer lowStockThreshold;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    @Setter(AccessLevel.NONE)
    private List<SaleItem> saleItems;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @Version
    private Long version;
}
