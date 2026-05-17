package com.sakute.project_fumo_backend.domain.dto.donation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DonationRequest {
    private UUID fundraisingId;
    @Min(10)
    @Max(50000)
    private BigDecimal amount;
    private Boolean isAnonymous;
}

