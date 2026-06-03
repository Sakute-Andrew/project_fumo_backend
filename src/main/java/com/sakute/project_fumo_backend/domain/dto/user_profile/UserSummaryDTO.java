package com.sakute.project_fumo_backend.domain.dto.user_profile;

import java.util.UUID;

public record UserSummaryDTO(
        UUID userId,
        String email,
        String username,
        String fullName,
        String bio,
        String areasOfExpertise,
        String location,
        String website,
        boolean canFundraise,
        boolean canSellIp
) {
}