package com.sakute.project_fumo_backend.domain.dto.user_profile;

import java.sql.Timestamp;
import java.util.UUID;

public record PostSummaryDTO(
    UUID postId,
    String postHeader,
    String postDescription,
    String photo,
    Timestamp createdAt,
    int likesCount,
    int commentsCount
) {}