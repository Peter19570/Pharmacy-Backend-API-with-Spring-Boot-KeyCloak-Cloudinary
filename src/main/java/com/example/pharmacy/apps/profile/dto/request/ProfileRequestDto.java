package com.example.pharmacy.apps.profile.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProfileRequestDto(
        @Min(0)
        @Max(150)
        Integer age,

        String city,

        String country,

        @Size(min = 10, max = 15)
        String contact,

        String bio
) {
}
