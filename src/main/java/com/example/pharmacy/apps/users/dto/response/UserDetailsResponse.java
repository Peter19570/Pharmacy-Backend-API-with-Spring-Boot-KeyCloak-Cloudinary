package com.example.pharmacy.apps.users.dto.response;

import java.time.Instant;

public record UserDetailsResponse(
        String email,
        String firstName,
        String lastName,
        String fullName,
        Integer age,
        String city,
        String country,
        String contact,
        String bio,
        String profilePhoto,
        Instant createdAt

) {
}
