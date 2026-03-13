package com.example.pharmacy.apps.profile.mapper;

import com.example.pharmacy.apps.common.dto.response.CloudinaryResponse;
import com.example.pharmacy.apps.profile.dto.response.ProfilePhotoResponse;
import com.example.pharmacy.apps.profile.model.ProfilePhoto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfilePhotoMapper {

    ProfilePhotoResponse toDto(ProfilePhoto profilePhoto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "url", source = "responseDto.url")
    @Mapping(target = "publicId", source = "responseDto.publicId")
    void toEntityFromDto(CloudinaryResponse responseDto, @MappingTarget ProfilePhoto profilePhoto);
}
