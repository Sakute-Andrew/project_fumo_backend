package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonationRequest;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonationResponse;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonationStatsDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonorDisplayDto;
import com.sakute.project_fumo_backend.domain.service.impl.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @PostMapping("/process")
    public ResponseEntity<DonationResponse> processDonation(
            @RequestBody DonationRequest request) {

        DonationResponse response = donationService.processMockDonation(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/fundraising/{fundraisingId}/donors")
    public ResponseEntity<List<DonorDisplayDto>> getRecentDonors(
            @PathVariable UUID fundraisingId,
            @RequestParam(defaultValue = "20") int limit) {

        List<DonorDisplayDto> donors = donationService.getDonors(fundraisingId);
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/fundraising/{fundraisingId}/stats")
    public ResponseEntity<DonationStatsDto> getDonationStats(
            @PathVariable UUID fundraisingId) {

        DonationStatsDto stats = donationService.getDonationStats(fundraisingId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/fundraising/{fundraisingId}/top-donors")
    public ResponseEntity<List<DonorDisplayDto>> getTopDonors(
            @PathVariable UUID fundraisingId,
            @RequestParam(defaultValue = "10") int limit) {

        // Тут би була логіка для топ-донатерів
         List<DonorDisplayDto> topDonors = donationService.getDonors(fundraisingId);
        return ResponseEntity.ok(topDonors);
    }
}
