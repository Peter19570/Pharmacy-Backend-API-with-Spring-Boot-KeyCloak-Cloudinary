package com.example.pharmacy.apps.users.service;

import com.example.pharmacy.apps.common.helper.UserState;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponseDto;
import com.example.pharmacy.apps.users.dto.response.UserResponseDto;
import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.apps.users.repo.UserRepo;
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

    public Page<UserResponseDto> allUsers(Pageable pageable){
        Page<User> userPage = userRepo.findAll(pageable);
        return userPage.map(userMapper::toDto);
    }

    public UserDetailsResponseDto getUser(Jwt jwt){
        return userMapper.toDetailsDto(userState.getCurrentUser(jwt));
    }

}
