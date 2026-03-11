package com.example.pharmacy.apps.users.repo;

import com.example.pharmacy.apps.users.model.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByKeyCloakId(String keyCloakId);

    Boolean existsByKeyCloakId(String keyCloakId);

    Page<User> findAll(@NonNull Pageable pageable);
}
