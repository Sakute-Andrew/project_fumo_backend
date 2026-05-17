package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.donation.DonationRequest;
import com.sakute.project_fumo_backend.domain.dto.donation.DonationResponse;
import com.sakute.project_fumo_backend.domain.dto.donation.DonationStatsDto;
import com.sakute.project_fumo_backend.domain.dto.donation.DonorDisplayDto;
// Припускаємо, що у вас є або буде DonationListDto для адмінки
import com.sakute.project_fumo_backend.domain.dto.donation.DonationListDto;
import com.sakute.project_fumo_backend.domain.service.impl.DonationService;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    // ==========================================
    // АДМІНСЬКА ЧАСТИНА (для Vue 3 useCrud)
    // ==========================================

    // Отримання списку донатів (з підтримкою фільтрації по збору)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DonationListDto>> getAllDonations(
            @RequestParam(required = false) UUID fundraisingId,
            Pageable pageable) {

        Page<DonationListDto> donations = donationService.getAllDonationsForAdmin(fundraisingId, pageable);
        return ResponseEntity.ok(donations);
    }

    // Видалення донату (реалізовано)
    @DeleteMapping("/{donationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDonation(@PathVariable UUID donationId) throws NotFoundException {
        donationService.deleteDonation(donationId);
        return ResponseEntity.noContent().build();
    }


    // ==========================================
    // ПУБЛІЧНА ЧАСТИНА ТА ЛОГІКА КОРИСТУВАЧІВ
    // ==========================================

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Тільки авторизовані можуть донатити
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

        List<DonorDisplayDto> topDonors = donationService.getTopDonors(fundraisingId, limit);
        return ResponseEntity.ok(topDonors);
    }
}