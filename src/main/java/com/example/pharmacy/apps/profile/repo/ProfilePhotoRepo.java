package com.example.pharmacy.apps.profile.repo;

import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfilePhotoRepo  extends JpaRepository<ProfilePhoto, UUID> {

    Optional<ProfilePhoto> findByProfile(Profile profile);
}
