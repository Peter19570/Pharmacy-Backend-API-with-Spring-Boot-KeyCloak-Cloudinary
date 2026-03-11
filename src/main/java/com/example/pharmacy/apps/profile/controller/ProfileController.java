package com.example.pharmacy.apps.profile.controller;

import com.cloudinary.Api;
import com.example.pharmacy.apps.common.dto.response.ApiResponseDto;
import com.example.pharmacy.apps.profile.dto.request.ProfileRequestDto;
import com.example.pharmacy.apps.profile.service.ProfilePhotoService;
import com.example.pharmacy.apps.profile.service.ProfileService;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfilePhotoService profilePhotoService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserDetailsResponseDto>> createProfile(
            @RequestBody ProfileRequestDto requestDto,
            @AuthenticationPrincipal Jwt jwt){
        UserDetailsResponseDto responseDto = profileService.createProfile(requestDto, jwt);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("Profile created", responseDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserDetailsResponseDto>> updateProfile(
            @RequestBody ProfileRequestDto requestDto,
            @AuthenticationPrincipal Jwt jwt){
        UserDetailsResponseDto responseDto = profileService.updateUser(jwt, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseDto<>("Profile Updated", responseDto));
    }


    @PostMapping("/profile/photo")
    public ResponseEntity<ApiResponseDto<UserDetailsResponseDto>> uploadProfile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ){
        UserDetailsResponseDto responseDto = profilePhotoService.uploadProfilePicture(jwt,file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseDto<>("Profile Photo Uploaded", responseDto));
    }
}
