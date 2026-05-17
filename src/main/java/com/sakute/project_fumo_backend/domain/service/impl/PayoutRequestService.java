package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;

import com.sakute.project_fumo_backend.domain.dto.PayoutRequestDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.enteties.PayoutRequest;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.repository.jpa_repo.DonationRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.PayoutRequestRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayoutRequestService {

    private final DonationRepository donationRepository;
    private final PayoutRequestRepository payoutRepository;
    private final FundraisingRepository fundraisingRepository;

    @Transactional(readOnly = true)
    public Page<PayoutRequestDto> getAll(Pageable pageable) {
        return payoutRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional
    public PayoutRequestDto updateStatus(UUID id, String status) {
        PayoutRequest request = payoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запит не знайдено"));

        // Просто міняємо статус запиту (PENDING -> COMPLETED або REJECTED)
        request.setStatus(status);

        // Ніяких сетерів для суми фандрейзингу! База сама все порахує динамічно
        return toDto(payoutRepository.save(request));
    }

    private PayoutRequestDto toDto(PayoutRequest entity) {
        PayoutRequestDto dto = new PayoutRequestDto();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setCardNumber(entity.getCardNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        if (entity.getFundraising() != null) {
            dto.setFundraisingId(entity.getFundraising().getId());
            dto.setFundraisingTitle(entity.getFundraising().getTitle());
        }
        return dto;
    }

    // У вашому FundraisingService (або там, де ви мапите DTO)


}