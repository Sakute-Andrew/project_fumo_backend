package com.sakute.project_fumo_backend.domain.dto.user_profile;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record FundraisingSummaryDTO(
    UUID fundraisingId,
    String title,
    String description,
    BigDecimal goalAmount,
    BigDecimal currentAmount,
    Timestamp endDate,
    Fundraising.Status status
) {}