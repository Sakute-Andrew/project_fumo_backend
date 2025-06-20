package com.sakute.project_fumo_backend.domain.enteties.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DonationStatsDto {
    private BigDecimal totalAmount;
    private Integer totalDonors;
    private BigDecimal averageDonation;
    private BigDecimal largestDonation;
}