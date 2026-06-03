package com.sakute.project_fumo_backend.domain.dto.fundraising;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundraisingDto {
    private UUID id;
    private String title;
    private String ownerUsername;
    private String description;
    private BigDecimal goalAmount;
    private BigDecimal currentAmount;
    private Timestamp endDate;
    private Timestamp createdAt;
    private Long category;
    private String status;
    private String userName;
    private int progressPercentage;
    private int daysLeft;
    private boolean isActive;
}
