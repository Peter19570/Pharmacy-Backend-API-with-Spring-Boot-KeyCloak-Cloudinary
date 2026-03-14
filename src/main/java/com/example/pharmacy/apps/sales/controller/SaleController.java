package com.example.pharmacy.apps.sales.controller;

import com.example.pharmacy.apps.common.dto.response.ApiResponse;
import com.example.pharmacy.apps.sales.dto.request.SaleRequest;
import com.example.pharmacy.apps.sales.dto.response.SaleDetailsResponse;
import com.example.pharmacy.apps.sales.dto.response.SaleResponse;
import com.example.pharmacy.apps.sales.filter.SaleFilter;
import com.example.pharmacy.apps.sales.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/sales")
    public ResponseEntity<ApiResponse<SaleDetailsResponse>> createSale(
            @RequestBody @Valid SaleRequest request,
            @AuthenticationPrincipal Jwt jwt){
        SaleDetailsResponse response = saleService.createSale(request, jwt);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Sale Created", response));
    }

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<Page<SaleResponse>>> getSales(
            @RequestParam(required = false) String transactionCode,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Instant createdBefore,
            @RequestParam(required = false) Instant createdAfter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        SaleFilter filter = SaleFilter.builder()
                .transactionCode(transactionCode)
                .paymentMethod(paymentMethod)
                .createdBefore(createdBefore)
                .createdAfter(createdAfter)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<SaleResponse> response = saleService.getAllSales(filter, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("All Sales", response));

    }

    @GetMapping("/sales/{transCode}")
    public ResponseEntity<ApiResponse<SaleDetailsResponse>> getSale(
            @PathVariable String transCode){
        SaleDetailsResponse response = saleService.getSale(transCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Sale Details", response));
    }

    @DeleteMapping("/sales/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSale(@PathVariable UUID id){
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
