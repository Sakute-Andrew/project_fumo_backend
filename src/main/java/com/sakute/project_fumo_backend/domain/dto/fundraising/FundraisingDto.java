package com.sakute.project_fumo_backend.domain.dto.fundraising;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
    private UUID ownerUserId;
    private String ownerUsername;
    private String description;
    @DecimalMin(value = "100", message = "Мінімальна сума збору — 100 ₴")
    @DecimalMax(value = "1000000", message = "Максимальна сума збору — 1 000 000 ₴")
    private BigDecimal goalAmount;
    private BigDecimal currentAmount;
    private Timestamp endDate;
    private Timestamp createdAt;
    private String category;
    private String status;
    private String userName;
    private BigDecimal withdrawnAmount;
    private int progressPercentage;
    private int daysLeft;
    private boolean isActive;
}
