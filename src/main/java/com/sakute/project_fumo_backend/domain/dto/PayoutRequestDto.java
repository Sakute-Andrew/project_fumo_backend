package com.sakute.project_fumo_backend.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class PayoutRequestDto {
    private UUID id;
    private BigDecimal amount;
    private String cardNumber;
    private Timestamp createdAt;
    private String status;
    private UUID fundraisingId;
    private String fundraisingTitle; // щоб адмін бачив, з якого саме збору вивід
}