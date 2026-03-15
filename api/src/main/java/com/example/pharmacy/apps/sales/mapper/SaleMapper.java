package com.example.pharmacy.apps.sales.mapper;

import com.example.pharmacy.apps.common.helper.TransCodeGenerator;
import com.example.pharmacy.apps.sales.dto.request.SaleRequest;
import com.example.pharmacy.apps.sales.dto.response.SaleDetailsResponse;
import com.example.pharmacy.apps.sales.dto.response.SaleResponse;
import com.example.pharmacy.apps.sales.model.Sale;
import org.mapstruct.*;
import org.springframework.security.oauth2.jwt.Jwt;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "saleItems", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "changeDue", ignore = true)
    @Mapping(target = "user", ignore = true)
    Sale toEntity(SaleRequest request);

    SaleResponse toDto(Sale sale);

    SaleDetailsResponse toDetailsDto(Sale sale);
}
