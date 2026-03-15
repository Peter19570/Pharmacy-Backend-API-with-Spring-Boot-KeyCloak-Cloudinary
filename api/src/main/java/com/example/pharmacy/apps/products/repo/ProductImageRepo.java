package com.example.pharmacy.apps.products.repo;

import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.products.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, UUID> {

    Optional<ProductImage> findByProduct(Product product);
}
