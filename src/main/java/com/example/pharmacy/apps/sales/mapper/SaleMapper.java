package com.example.pharmacy.apps.sales.mapper;

import com.example.pharmacy.apps.common.helper.TransCodeGenerator;
import com.example.pharmacy.apps.sales.dto.request.SaleRequest;
import com.example.pharmacy.apps.sales.dto.response.SaleDetailsResponse;
import com.example.pharmacy.apps.sales.dto.response.SaleResponse;
import com.example.pharmacy.apps.sales.model.Sale;
import org.mapstruct.*;
import org.springframework.security.oauth2.jwt.Jwt;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {TransCodeGenerator.class}
)
public interface SaleMapper {

    @Mapping(target = "transactionCode", expression = "java(TransCodeGenerator.generateTransactionCode())")
    Sale toEntity(SaleRequest request);

    SaleResponse toDto(Sale sale);

    SaleDetailsResponse toDetailsDto(Sale sale);

    @AfterMapping
    default void linkSaleToSaleItem(@MappingTarget Sale sale){
        if (sale.getSaleItems() != null){
            sale.getSaleItems().forEach(saleItem -> saleItem.setSale(sale));
        }
    }
}
