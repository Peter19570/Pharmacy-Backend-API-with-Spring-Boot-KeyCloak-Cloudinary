package com.example.pharmacy.apps.profile.controller;

import com.example.pharmacy.apps.common.dto.response.ApiResponse;
import com.example.pharmacy.apps.profile.dto.request.ProfileRequest;
import com.example.pharmacy.apps.profile.dto.response.ProfilePhotoResponse;
import com.example.pharmacy.apps.profile.service.ProfilePhotoService;
import com.example.pharmacy.apps.profile.service.ProfileService;
import com.example.pharmacy.apps.users.dto.response.UserDetailsResponse;
import com.example.pharmacy.apps.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfilePhotoService profilePhotoService;
    private final UserService userService;

    @GetMapping("/profiles/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserAndProfileDetails(
            @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("User Details", userService.getUser(jwt)));
    }

    @PutMapping("/profiles/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateProfileDetails(
            @RequestBody ProfileRequest requestDto,
            @AuthenticationPrincipal Jwt jwt){
        UserDetailsResponse responseDto = profileService.updateUser(jwt, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Profile Updated", responseDto));
    }


    @PostMapping("/profiles/me/photo")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> uploadProfilePhoto(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ){
        UserDetailsResponse responseDto = profilePhotoService.uploadProfilePicture(jwt,file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Profile Photo Uploaded", responseDto));
    }

    @DeleteMapping("/profiles/me/photo")
    public ResponseEntity<Void> deleteProfilePhoto(
            @AuthenticationPrincipal Jwt jwt){
        profilePhotoService.removeProfilePhoto(jwt);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profiles/photo/{id}")
    public ResponseEntity<ApiResponse<ProfilePhotoResponse>> getProfilePhoto(
            @PathVariable UUID id){
        ProfilePhotoResponse responseDto = profilePhotoService.getProfilePhoto(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("Profile Photo", responseDto));
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getOtherUser(@PathVariable UUID id){
        UserDetailsResponse responseDto = userService.getOtherUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("User Details", responseDto));
    }
}
