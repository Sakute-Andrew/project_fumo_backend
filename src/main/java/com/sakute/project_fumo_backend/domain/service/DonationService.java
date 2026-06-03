package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.dto.donation.*;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface DonationService extends Service<Donation, UUID> {

    DonationResponse processMockDonation(DonationRequest request);

    Page<DonationListDto> getAllDonationsForAdmin(UUID fundraisingId, Pageable pageable);

    void deleteDonation(UUID donationId) throws NotFoundException, javassist.NotFoundException;

    DonationStatsDto getDonationStats(UUID fundraisingId);

    List<DonorDisplayDto> getDonors(UUID fundraisingId);

    List<DonorDisplayDto> getTopDonors(UUID fundraisingId, int limit);

}
