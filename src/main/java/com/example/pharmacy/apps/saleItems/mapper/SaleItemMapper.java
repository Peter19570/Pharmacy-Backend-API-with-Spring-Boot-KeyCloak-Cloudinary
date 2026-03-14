package com.example.pharmacy.apps.saleItems.mapper;

import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.saleItems.dto.request.SaleItemRequest;
import com.example.pharmacy.apps.saleItems.dto.response.SaleItemResponse;
import com.example.pharmacy.apps.saleItems.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {BigDecimal.class})
public interface SaleItemMapper {

    @Mapping(target = "unitPrice", source = "product.sellingPrice")
    @Mapping(target = "totalPrice",
            expression = "java(product.getSellingPrice().multiply(BigDecimal.valueOf(request.quantitySold())))")
    SaleItem toEntity(SaleItemRequest request, Product product);

    SaleItemResponse toDto(SaleItem saleItem);
}
