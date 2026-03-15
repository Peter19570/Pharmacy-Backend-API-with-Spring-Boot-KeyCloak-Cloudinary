package com.example.pharmacy.apps.sales.service;

import com.example.pharmacy.apps.common.helper.TransCodeGenerator;
import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.products.repo.ProductRepo;
import com.example.pharmacy.apps.saleItems.dto.request.SaleItemRequest;
import com.example.pharmacy.apps.saleItems.dto.response.SaleItemResponse;
import com.example.pharmacy.apps.saleItems.mapper.SaleItemMapper;
import com.example.pharmacy.apps.saleItems.model.SaleItem;
import com.example.pharmacy.apps.saleItems.repo.SaleItemRepo;
import com.example.pharmacy.apps.sales.dto.request.SaleRequest;
import com.example.pharmacy.apps.sales.dto.response.SaleDetailsResponse;
import com.example.pharmacy.apps.sales.dto.response.SaleResponse;
import com.example.pharmacy.apps.sales.filter.SaleFilter;
import com.example.pharmacy.apps.sales.mapper.SaleMapper;
import com.example.pharmacy.apps.sales.model.Sale;
import com.example.pharmacy.apps.sales.repo.SaleRepo;
import com.example.pharmacy.exception.custom.LowCountException;
import com.example.pharmacy.exception.custom.NotEnoughException;
import com.example.pharmacy.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaleService {

    private final SaleMapper saleMapper;
    private final SaleRepo saleRepo;
    private final SaleItemMapper saleItemMapper;
    private final SaleItemRepo saleItemRepo;
    private final ProductRepo productRepo;
    private final UserState userState;

    @Transactional
    public SaleDetailsResponse createSale(SaleRequest request, Jwt jwt) {

        List<UUID> productIds = request.saleItems().stream()
                .map(SaleItemRequest::productId)
                .sorted()
                .toList();

        List<Product> products = productRepo.findAllByIdWithLock(productIds);

        if (products.size() != productIds.size()) {
            throw new NotFoundException("One or more products not found");
        }

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        for (SaleItemRequest item : request.saleItems()) {
            Product product = productMap.get(item.productId());
            if (product.getQuantity() < item.quantitySold()) {
                throw new LowCountException(
                        product.getName() + " insufficient, " + product.getQuantity() + " available"
                );
            }
        }

        Sale sale = saleMapper.toEntity(request);
        sale.setUser(userState.getCurrentUser(jwt));
        sale.setTransactionCode(TransCodeGenerator.generateTransactionCode());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleItemRequest itemRequest : request.saleItems()) {
            Product product = productMap.get(itemRequest.productId());

            SaleItem saleItem = saleItemMapper.toEntity(itemRequest);
            saleItem.setUnitPrice(product.getSellingPrice());
            saleItem.setTotalPrice(product.getSellingPrice().multiply(BigDecimal.valueOf(itemRequest.quantitySold())));
            saleItem.setSale(sale);
            saleItem.setProduct(product);
            sale.getSaleItems().add(saleItem);

            product.setQuantity(product.getQuantity() - itemRequest.quantitySold());

            totalAmount = totalAmount.add(
                    product.getSellingPrice().multiply(BigDecimal.valueOf(itemRequest.quantitySold()))
            );
        }

        sale.setTotalAmount(totalAmount);

        if (request.amountPaid().compareTo(totalAmount) < 0){
            throw new NotEnoughException("Not enough funds");
        }

        sale.setChangeDue(request.amountPaid().subtract(totalAmount));

        Sale savedSale = saleRepo.saveAndFlush(sale);
        return saleMapper.toDetailsDto(savedSale);
    }

    public Page<SaleResponse> getAllSales(SaleFilter filter, Pageable pageable){

        Specification<Sale> specification = Specification.allOf();

        if (filter.getTransactionCode() != null){
            specification = specification.and(hasTransCode(filter.getTransactionCode()));
        }

        if (filter.getPaymentMethod() != null){
            specification = specification.and(hasPaymentMethod(filter.getPaymentMethod()));
        }

        if (filter.getCreatedBefore() != null || filter.getCreatedAfter() != null){
            specification = specification.and(hasDate(filter.getCreatedBefore(), filter.getCreatedAfter()));
        }

        Page<Sale> salePage = saleRepo.findAll(specification, pageable);
        return salePage.map(saleMapper::toDto);
    }

    public SaleDetailsResponse getSale(String transactionCode){
        Sale sale = saleRepo.findByTransactionCode(transactionCode)
                .orElseThrow(()-> new NotFoundException("Sale not found"));
        return saleMapper.toDetailsDto(sale);
    }

    @Transactional
    public void deleteSale(UUID id){
        Sale sale = saleRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Sale not found"));
        saleRepo.delete(sale);
    }

    private Specification<Sale> hasTransCode(String transactionCode){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("transactionCode"), transactionCode);
        });
    }

    private Specification<Sale> hasPaymentMethod(String paymentMethod){
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("paymentMethod")), "%" + paymentMethod.toLowerCase() + "%");
        });
    }

    private Specification<Sale> hasDate(Instant createdBefore, Instant createdAfter){
        return ((root, query, criteriaBuilder) -> {
            if (createdBefore != null && createdAfter != null){
                return criteriaBuilder.between(root.get("createdAt"), createdBefore, createdAfter);
            } else if (createdBefore != null) {
                return criteriaBuilder.lessThan(root.get("createdAt"), createdBefore);
            } else if (createdAfter != null) {
                return criteriaBuilder.greaterThan(root.get("createdAt"), createdAfter);
            }
            return criteriaBuilder.conjunction();
        });
    }
}
