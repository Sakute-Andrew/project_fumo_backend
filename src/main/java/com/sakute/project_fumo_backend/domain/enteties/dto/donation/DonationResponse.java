package com.sakute.project_fumo_backend.domain.enteties.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DonationResponse {
    private boolean success;
    private String message;
    private String transactionId;
}
