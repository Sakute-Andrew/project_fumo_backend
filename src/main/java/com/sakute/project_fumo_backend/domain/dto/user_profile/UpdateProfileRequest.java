package com.sakute.project_fumo_backend.domain.dto.user_profile;

public record UpdateProfileRequest(
    String bio,
    String areasOfExpertise,
    String website,
    String location
) {}