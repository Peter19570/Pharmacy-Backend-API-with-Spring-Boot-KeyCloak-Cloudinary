package com.example.pharmacy.apps.saleItems.model;

import com.example.pharmacy.apps.common.model.BaseEntity;
import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.sales.model.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sale_items")
public class SaleItem extends BaseEntity {

    @Column(nullable = false)
    private Integer quantitySold;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Sale sale;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
