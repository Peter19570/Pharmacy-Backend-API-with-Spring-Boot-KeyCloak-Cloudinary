package com.example.pharmacy.apps.users.mapper;

import com.example.pharmacy.apps.users.dto.response.UserDetailsResponse;
import com.example.pharmacy.apps.users.dto.response.UserResponse;
import com.example.pharmacy.apps.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "keyCloakId", expression = "java(jwt.getSubject())")
    @Mapping(target = "email", expression = "java(jwt.getClaimAsString(\"email\"))")
    @Mapping(target = "firstName", expression = "java(jwt.getClaimAsString(\"given_name\"))")
    @Mapping(target = "lastName", expression = "java(jwt.getClaimAsString(\"family_name\"))")
    @Mapping(target = "id", ignore = true)
    User toEntity(Jwt jwt);

    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "age", source = "profile.age")
    @Mapping(target = "city", source = "profile.city")
    @Mapping(target = "country", source = "profile.country")
    @Mapping(target = "contact", source = "profile.contact")
    @Mapping(target = "bio", source = "profile.bio")
    @Mapping(target = "profilePhoto", source = "profile.profilePhoto.url")
    UserDetailsResponse toDetailsDto(User user);

    UserResponse toDto(User user);
}
