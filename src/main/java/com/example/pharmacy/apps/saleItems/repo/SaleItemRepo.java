package com.example.pharmacy.apps.saleItems.repo;

import com.example.pharmacy.apps.saleItems.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SaleItemRepo extends JpaRepository<SaleItem, UUID> {
}
