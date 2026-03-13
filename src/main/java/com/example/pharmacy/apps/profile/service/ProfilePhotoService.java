package com.example.pharmacy.apps.profile.service;

import com.example.pharmacy.apps.common.dto.response.CloudinaryResponse;
import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.common.service.CloudinaryService;
import com.example.pharmacy.apps.profile.dto.response.ProfilePhotoResponse;
import com.example.pharmacy.apps.profile.mapper.ProfilePhotoMapper;
import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import com.example.pharmacy.apps.profile.repo.ProfilePhotoRepo;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponse;
import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.exception.custom.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private final CloudinaryService cloudinaryService;
    private final ProfilePhotoRepo profilePhotoRepo;
    private final UserState userState;
    private final UserMapper userMapper;
    private final ProfilePhotoMapper profilePhotoMapper;

    @Transactional
    public UserDetailsResponse uploadProfilePicture(Jwt jwt, MultipartFile file){
        User user = userState.getCurrentUser(jwt);
        Profile profile = userState.getCurrentUserProfile(jwt);
        ProfilePhoto profilePhoto = profilePhotoRepo.findByProfile(profile)
                .orElseThrow(()-> new NotFoundException("Profile Photo not found"));

        String oldProfilePhoto = profilePhoto.getPublicId();

        if (!(oldProfilePhoto == null)){
            cloudinaryService.delete(oldProfilePhoto);
        }

        CloudinaryResponse responseDto = cloudinaryService
                .upload(file, "user_profile_photos");

        profilePhotoMapper.toEntityFromDto(responseDto, profilePhoto);
        return userMapper.toDetailsDto(user);
    }

    public ProfilePhotoResponse getProfilePhoto(UUID id){
        ProfilePhoto profilePhoto = profilePhotoRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Profile photo not found"));
        return profilePhotoMapper.toDto(profilePhoto);
    }

    @Transactional
    public void removeProfilePhoto(Jwt jwt){
        Profile profile = userState.getCurrentUserProfile(jwt);
        ProfilePhoto profilePhoto = profilePhotoRepo.findByProfile(profile)
                .orElseThrow(()-> new NotFoundException("Profile Photo not found"));
        cloudinaryService.delete(profilePhoto.getPublicId());
        profilePhoto.setPublicId(null);
        profilePhoto.setUrl(null);
    }
}
