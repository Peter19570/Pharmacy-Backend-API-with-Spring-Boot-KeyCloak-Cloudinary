package com.example.pharmacy.apps.profile.mapper;

import com.example.pharmacy.apps.profile.dto.request.ProfileRequest;
import com.example.pharmacy.apps.profile.model.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    Profile toEntity(ProfileRequest requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromDto(ProfileRequest requestDto, @MappingTarget Profile profile);
}
