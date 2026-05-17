package com.sakute.project_fumo_backend.domain.dto.user_profile;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;

import java.util.UUID;

public record IpSummaryDTO(
    UUID ipId,
    String name,
    String description,
    String typeIp,
    IpStatus status
) {}