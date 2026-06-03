package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;

import com.sakute.project_fumo_backend.domain.dto.PayoutRequestDto;
import com.sakute.project_fumo_backend.domain.dto.PermissionRequestDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.enteties.PayoutRequest;
import com.sakute.project_fumo_backend.domain.enteties.PermissionRequest;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayoutRequestService {

    private final DonationRepository donationRepository;
    private final PayoutRequestRepository payoutRepository;
    private final FundraisingRepository fundraisingRepository;
    private final PermissionRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<PayoutRequestDto> getAll(Pageable pageable) {
        return payoutRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional
    public PayoutRequestDto updateStatus(UUID id, RequestStatus status) {
        PayoutRequest request = payoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявку не знайдено"));

        request.setStatus(status);
        return toDto(payoutRepository.save(request));
    }

    private PayoutRequestDto toDto(PayoutRequest entity) {
        PayoutRequestDto dto = new PayoutRequestDto();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setCardNumber(entity.getCardNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus().toString());
        if (entity.getFundraising() != null) {
            dto.setFundraisingId(entity.getFundraising().getId());
            dto.setFundraisingTitle(entity.getFundraising().getTitle());
        }
        return dto;
    }


    @Transactional
    public PayoutRequestDto create(PayoutRequestDto dto) {
        Fundraising fundraising = fundraisingRepository.findById(dto.getFundraisingId())
                .orElseThrow(() -> new NotFoundException("Фандрейзинг не знайдено"));

        // Перевірка що є що виводити
        BigDecimal available = donationRepository.sumByFundraisingId(fundraising.getId());
        if (available == null || available.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Немає коштів для виводу");
        }

        PayoutRequest request = new PayoutRequest();
        request.setFundraising(fundraising);
        request.setAmount(dto.getAmount());
        request.setCardNumber(dto.getCardNumber());
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return toDto(payoutRepository.save(request));
    }

    // У вашому FundraisingService (або там, де ви мапите DTO)


}