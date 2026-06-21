package com.sakute.project_fumo_backend.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class PayoutRequestDto {
    private UUID id;

    @NotNull(message = "Сума обов'язкова")
    @DecimalMin(value = "1", message = "Мінімальна сума виводу — 1 ₴")
    private BigDecimal amount;

    @NotNull(message = "Номер картки обов'язковий")
    @Pattern(regexp = "\\d{16}", message = "Номер картки має містити 16 цифр")
    private String cardNumber;

    private Timestamp createdAt;
    private String status;

    @NotNull(message = "Не вказано збір")
    private UUID fundraisingId;
    private String fundraisingTitle;
}