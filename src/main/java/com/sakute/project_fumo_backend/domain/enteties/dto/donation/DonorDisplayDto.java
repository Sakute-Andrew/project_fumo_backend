package com.sakute.project_fumo_backend.domain.enteties.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
public class DonorDisplayDto {
    private String displayName;
    private BigDecimal amount;
    private Timestamp donatedAt;
    private Boolean isAnonymous;
}
