package com.example.pharmacy.apps.profile.service;

import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.profile.dto.request.ProfileRequestDto;
import com.example.pharmacy.apps.profile.mapper.ProfileMapper;
import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import com.example.pharmacy.apps.profile.repo.ProfilePhotoRepo;
import com.example.pharmacy.apps.profile.repo.ProfileRepo;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponseDto;
import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.apps.users.repo.UserRepo;
import com.example.pharmacy.exception.custom.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepo profileRepo;
    private final ProfilePhotoRepo profilePhotoRepo;
    private final UserRepo userRepo;
    private final ProfileMapper profileMapper;
    private final UserState userState;
    private final UserMapper userMapper;

    @Transactional
    public UserDetailsResponseDto createProfile(
            ProfileRequestDto requestDto,Jwt jwt){
        User user = userState.getOrCreateUser(jwt);
        Profile profile = profileMapper.toEntity(requestDto);

        ProfilePhoto profilePhoto = new ProfilePhoto();

        user.setProfile(profile);
        profile.setUser(user);

        profile.setProfilePhoto(profilePhoto);
        profilePhoto.setProfile(profile);

        profilePhotoRepo.saveAndFlush(profilePhoto);
        profileRepo.saveAndFlush(profile);
        userRepo.saveAndFlush(user);

        return userMapper.toDetailsDto(user);
    }

    @Transactional
    public UserDetailsResponseDto updateUser(Jwt jwt, ProfileRequestDto requestDto){
        User user = userState.getCurrentUser(jwt);
        Profile profile = profileRepo.findByUser(user)
                .orElseThrow(()-> new NotFoundException("Profile not found"));
        profileMapper.toEntityFromDto(requestDto, profile);
        return userMapper.toDetailsDto(user);
    }
}
