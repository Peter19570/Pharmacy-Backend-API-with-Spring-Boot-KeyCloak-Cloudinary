package com.example.pharmacy.apps.products.mapper;

import com.example.pharmacy.apps.products.dto.request.ProductRequest;
import com.example.pharmacy.apps.products.dto.response.ProductDetailsResponse;
import com.example.pharmacy.apps.products.dto.response.ProductImageDetailsResponse;
import com.example.pharmacy.apps.products.dto.response.ProductResponse;
import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.products.model.ProductImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequest requestDto);

    ProductResponse toDto(Product product);

    ProductDetailsResponse toDetailDto(Product product);

    ProductImageDetailsResponse toImageDto(ProductImage productImage);

    @AfterMapping
    default void linkProductToImages(@MappingTarget Product product){
        if (product.getImages() != null){
            product.getImages().forEach(image -> image.setProduct(product));
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductRequest requestDto, @MappingTarget Product product);
}
