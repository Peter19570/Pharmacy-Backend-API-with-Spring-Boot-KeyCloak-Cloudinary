package com.example.pharmacy.apps.products.controller;

import com.example.pharmacy.apps.common.dto.response.ApiResponse;
import com.example.pharmacy.apps.products.dto.request.ProductRequest;
import com.example.pharmacy.apps.products.dto.response.ProductDetailsResponse;
import com.example.pharmacy.apps.products.dto.response.ProductImageDetailsResponse;
import com.example.pharmacy.apps.products.dto.response.ProductResponse;
import com.example.pharmacy.apps.products.filter.ProductFilter;
import com.example.pharmacy.apps.products.service.ProductImageService;
import com.example.pharmacy.apps.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> createProduct(
            @RequestBody @Valid ProductRequest requestDto){
        ProductDetailsResponse responseDto = productService.createProduct(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created", responseDto));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(required = false) Boolean expired,
            @RequestParam(required = false) Boolean lowStock,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        ProductFilter filter = ProductFilter.builder()
                .name(name)
                .category(category)
                .description(description)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .startDate(startDate)
                .endDate(endDate)
                .expired(expired)
                .lowStock(lowStock)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProductResponse> response = productService.getAllProducts(filter, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("All Available Products", response));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> getProductDetails(
            @PathVariable UUID id){
        ProductDetailsResponse responseDto = productService.getProductDetails(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Product Details", responseDto));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductRequest requestDto){
        ProductDetailsResponse responseDto = productService.updateProduct(id, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Product Updated", responseDto));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products/photo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> uploadProductImage(
            @RequestParam("file")MultipartFile file,
            @PathVariable  UUID id){
        productImageService.uploadProductImage(file, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/products/photo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductImage(
            @RequestParam("publicId") String publicId,
            @PathVariable UUID id
    ){
        productImageService.deleteProductImage(publicId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/photo/{id}")
    public ResponseEntity<ApiResponse<ProductImageDetailsResponse>> getProductImage(
            @PathVariable UUID id
    ){
        ProductImageDetailsResponse responseDto = productImageService.getProductImage(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Product Photo", responseDto ));
    }
}
