package com.example.pharmacy.apps.products.service;

import com.example.pharmacy.apps.products.dto.request.ProductRequest;
import com.example.pharmacy.apps.products.dto.response.ProductDetailsResponse;
import com.example.pharmacy.apps.products.dto.response.ProductResponse;
import com.example.pharmacy.apps.products.filter.ProductFilter;
import com.example.pharmacy.apps.products.mapper.ProductMapper;
import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.products.model.ProductImage;
import com.example.pharmacy.apps.products.repo.ProductImageRepo;
import com.example.pharmacy.apps.products.repo.ProductRepo;
import com.example.pharmacy.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDetailsResponse createProduct(ProductRequest requestDto){
        Product product = productMapper.toEntity(requestDto);
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImageRepo.save(productImage);
        Product savedProduct = productRepo.saveAndFlush(product);
        return productMapper.toDetailDto(savedProduct);
    }

    public ProductDetailsResponse getProductDetails(UUID id){
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        return productMapper.toDetailDto(product);
    }

    @Transactional
    public ProductDetailsResponse updateProduct(UUID id, ProductRequest requestDto){
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        productMapper.updateEntity(requestDto, product);
        return productMapper.toDetailDto(product);
    }

    @Transactional
    public void deleteProduct(UUID id){
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        productRepo.delete(product);
    }

    public Page<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable){
        Specification<Product> specification = Specification.allOf();

        if (filter.getName() != null){
            specification = specification.and(containsName(filter.getName()));
        }

        if (filter.getCategory() != null){
            specification = specification.and(hasCategory(filter.getCategory()));
        }

        if (filter.getDescription() != null){
            specification = specification.and(hasDescription(filter.getDescription()));
        }

        if (filter.getMinPrice() != null || filter.getMaxPrice() != null){
            specification = specification.and(betweenPrice(filter.getMinPrice(), filter.getMaxPrice()));
        }

        if (filter.getStartDate() != null || filter.getEndDate() != null){
            specification = specification.and(betweenDate(filter.getStartDate(), filter.getEndDate()));
        }

        if (filter.getExpired() != null && filter.getExpired()){
            specification = specification.and(hasExpired());
        }

        if (filter.getLowStock() != null && filter.getLowStock()){
            specification = specification.and(inLowStock());
        }

        Page<Product> productPage = productRepo.findAll(specification, pageable);
        return productPage.map(productMapper::toDto);
    }

    private Specification<Product> containsName(String name){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });
    }

    private Specification<Product> hasCategory(String category){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("category")), "%" + category.toLowerCase() + "%");
        });
    }

    private Specification<Product> hasDescription(String description){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        });
    }

    private Specification<Product> betweenPrice(BigDecimal minPrice, BigDecimal maxPrice){
        return ((root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null){
                return criteriaBuilder.between(root.get("sellingPrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), maxPrice);
            }
            return criteriaBuilder.conjunction();
        });
    }

    private Specification<Product> betweenDate(Instant startDate, Instant endDate){
        return ((root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null){
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), startDate);
            } else if (endDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), endDate);
            }
            return criteriaBuilder.conjunction();
        });
    }

    private Specification<Product> hasExpired(){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("expirationDate"), Instant.now());
        });
    }

    private Specification<Product> inLowStock(){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("quantity"),root.get("lowStockThreshold"));
        });
    }
}
