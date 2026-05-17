package com.sakute.project_fumo_backend.domain.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationListDto {
    private UUID id;
    private BigDecimal amount;
    private String transactionId;
    private Timestamp createdAt;
    private Boolean isAnonymous;
    
    // Плоскі поля для зручного відображення в таблиці на фронтенді
    private String donorName; 
    private String fundraisingTitle; 
}