package com.sakute.project_fumo_backend.domain.dto.user_profile;

import java.util.Set;
import java.util.UUID;

public record UserSummaryDTO(
    UUID userId,
    String username,
    String profilePicture,
    String bio,
    Set<String> tags,
    boolean canFundraise,
    boolean canSellIp
) {}