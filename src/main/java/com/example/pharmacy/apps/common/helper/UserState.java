package com.example.pharmacy.apps.common.helper;

import com.example.pharmacy.apps.users.mapper.UserMapper;
import com.example.pharmacy.apps.users.model.User;
import com.example.pharmacy.apps.users.repo.UserRepo;
import com.example.pharmacy.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserState {

    private final UserRepo userRepo;

    public User getCurrentUser(Jwt jwt){
        return userRepo.findByKeyCloakId(jwt.getSubject())
                .orElseThrow(()-> new NotFoundException("User Not Found"));
    }

}
