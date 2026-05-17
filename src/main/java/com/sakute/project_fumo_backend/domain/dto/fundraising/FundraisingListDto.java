package com.sakute.project_fumo_backend.domain.dto.fundraising;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class FundraisingListDto {

    private UUID id;
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull
    @Positive(message = "Goal amount must be greater than 0") // Гроші мають бути в плюсі
    private BigDecimal goalAmount;
    private BigDecimal currentAmount;
    private Timestamp endDate;
    private Timestamp createdAt;
    private Long category;
    private int progressPercentage;
    private int daysLeft;
}
