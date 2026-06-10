package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.PayoutRequestDto;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PayoutRequestService {
    Page<PayoutRequestDto> getAll(Pageable pageable);
    PayoutRequestDto updateStatus(UUID id, RequestStatus status);
    PayoutRequestDto create(PayoutRequestDto dto);
}
