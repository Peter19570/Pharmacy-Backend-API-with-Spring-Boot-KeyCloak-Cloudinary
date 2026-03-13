package com.example.pharmacy.apps.products.repo;

import com.example.pharmacy.apps.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

}
