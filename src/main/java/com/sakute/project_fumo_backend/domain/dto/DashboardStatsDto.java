package com.sakute.project_fumo_backend.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStatsDto {
    private long totalUsers;
    private long activeFundraisings;
    private BigDecimal totalDonatedAmount;
    private long pendingPayoutsCount;
    private long pendingPermissionsCount;
}