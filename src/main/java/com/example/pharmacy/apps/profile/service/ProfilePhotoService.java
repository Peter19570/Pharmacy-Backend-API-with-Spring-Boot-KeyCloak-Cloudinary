package com.example.pharmacy.apps.profile.service;

import com.example.pharmacy.apps.common.dto.response.CloudinaryResponseDto;
import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.common.service.CloudinaryService;
import com.example.pharmacy.apps.profile.mapper.ProfileMapper;
import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import com.example.pharmacy.apps.profile.repo.ProfilePhotoRepo;
import com.example.pharmacy.apps.profile.repo.ProfileRepo;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponseDto;
import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.exception.custom.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService {

    private final CloudinaryService cloudinaryService;
    private final ProfileRepo profileRepo;
    private final ProfilePhotoRepo profilePhotoRepo;
    private final UserState userState;
    private final UserMapper userMapper;

    @Transactional
    public UserDetailsResponseDto uploadProfilePicture(Jwt jwt, MultipartFile file){
        User user = userState.getCurrentUser(jwt);
        Profile profile = profileRepo.findByUser(user)
                .orElseThrow(()->new NotFoundException("User not found"));
        ProfilePhoto profilePhoto = profilePhotoRepo.findByProfile(profile)
                .orElseThrow(()-> new NotFoundException("Profile not found"));

        CloudinaryResponseDto responseDto = cloudinaryService.upload(file, "user_profile_photos");

        profilePhoto.setUrl(responseDto.url());
        profilePhoto.setPublicId(responseDto.publicId());
        profilePhotoRepo.saveAndFlush(profilePhoto);

        return userMapper.toDetailsDto(user);
    }
}
