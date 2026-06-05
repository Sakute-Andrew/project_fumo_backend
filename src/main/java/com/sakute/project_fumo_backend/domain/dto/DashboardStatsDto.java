package com.sakute.project_fumo_backend.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStatsDto {
    // Users
    private long totalUsers;

    // Fundraisings
    private long totalFundraisings;
    private long activeFundraisings;
    private long completedFundraisings;

    // Donations & payouts
    private long totalDonationsCount;
    private BigDecimal totalDonatedAmount;
    private long pendingPayoutsCount;
    private BigDecimal pendingPayoutAmount;

    // Intellectual property
    private long totalIpAssets;
    private long ipLegalTroublesCount;

    // Content
    private long totalPosts;

    // Pending requests
    private long pendingPermissionsCount;
}