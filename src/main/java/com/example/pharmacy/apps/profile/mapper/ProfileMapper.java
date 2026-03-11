package com.example.pharmacy.apps.profile.mapper;

import com.example.pharmacy.apps.profile.dto.request.ProfileRequestDto;
import com.example.pharmacy.apps.profile.model.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    Profile toEntity(ProfileRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityFromDto(ProfileRequestDto requestDto, @MappingTarget Profile profile);
}
