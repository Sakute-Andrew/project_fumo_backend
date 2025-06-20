package com.sakute.project_fumo_backend.domain.enteties.dto.donation;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DonationRequest {
    private UUID fundraisingId;
    private BigDecimal amount;
    private String donorName;
    private String donorEmail;
    private Boolean isAnonymous = false;
}

