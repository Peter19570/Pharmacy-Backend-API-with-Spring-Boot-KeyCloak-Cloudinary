package com.example.pharmacy.apps.users.service;

import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponse;
import com.example.pharmacy.apps.users.dto.response.UserResponse;
import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.apps.users.repo.UserRepo;
import com.example.pharmacy.exception.custom.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UserState userState;

    @Transactional
    public void createUser(Jwt jwt){
        if (!userRepo.existsByKeyCloakId(jwt.getSubject())){
            User user = userMapper.toEntity(jwt);
            Profile profile = new Profile();
            ProfilePhoto profilePhoto = new ProfilePhoto();

            profilePhoto.setProfile(profile);
            profile.setUser(user);
            profile.setProfilePhoto(profilePhoto);

            user.setProfile(profile);
            userRepo.save(user);
        }
    }

    public Page<UserResponse> allUsers(Pageable pageable){
        Page<User> userPage = userRepo.findAll(pageable);
        return userPage.map(userMapper::toDto);
    }

    public UserDetailsResponse getUser(Jwt jwt){
        return userMapper.toDetailsDto(userState.getCurrentUser(jwt));
    }

    public UserDetailsResponse getOtherUser(UUID id){
        User user = userRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("User not found"));
        return userMapper.toDetailsDto(user);
    }

}
