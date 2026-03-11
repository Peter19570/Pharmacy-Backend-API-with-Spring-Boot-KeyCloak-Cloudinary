package com.example.pharmacy.apps.users.service;

import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.profile.mapper.ProfileMapper;
import com.example.pharmacy.apps.profile.model.Profile;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import com.example.pharmacy.apps.profile.repo.ProfileRepo;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponseDto;
import com.example.pharmacy.apps.users.dto.response.UserResponseDto;
import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.apps.users.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

    public Page<UserResponseDto> allUsers(Pageable pageable){
        Page<User> userPage = userRepo.findAll(pageable);
        return userPage.map(userMapper::toDto);
    }

    public UserDetailsResponseDto getUser(Jwt jwt){
        return userMapper.toDetailsDto(userState.getCurrentUser(jwt));
    }

}
