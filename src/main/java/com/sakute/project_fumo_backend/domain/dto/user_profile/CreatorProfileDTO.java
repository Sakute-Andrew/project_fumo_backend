package com.sakute.project_fumo_backend.domain.dto.user_profile;

import java.util.List;

public record CreatorProfileDTO(
    UserSummaryDTO user,
    List<PostSummaryDTO> posts,
    List<FundraisingSummaryDTO> fundraisings,
    List<IpSummaryDTO> intellectualProperties
) {}