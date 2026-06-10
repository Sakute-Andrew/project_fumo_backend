package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.user_profile.CreatorProfileDTO;
import com.sakute.project_fumo_backend.domain.dto.user_profile.UpdateProfileRequest;

import java.util.UUID;

public interface CreatorProfileService {
    CreatorProfileDTO getProfile(UUID userId);
    void updateProfile(UUID userId, UpdateProfileRequest request);
}
