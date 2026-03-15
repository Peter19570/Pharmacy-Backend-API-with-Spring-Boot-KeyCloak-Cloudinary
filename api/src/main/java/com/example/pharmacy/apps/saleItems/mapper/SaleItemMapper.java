package com.example.pharmacy.apps.saleItems.mapper;

import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.saleItems.dto.request.SaleItemRequest;
import com.example.pharmacy.apps.saleItems.dto.response.SaleItemResponse;
import com.example.pharmacy.apps.saleItems.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    SaleItem toEntity(SaleItemRequest request);

    SaleItemResponse toDto(SaleItem saleItem);
}
