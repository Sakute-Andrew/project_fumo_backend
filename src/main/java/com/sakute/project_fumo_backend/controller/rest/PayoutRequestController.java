package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.PayoutRequestDto;
import com.sakute.project_fumo_backend.domain.service.impl.PayoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payouts") // Без api/v1
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Тільки адміни мають доступ до цієї секції
public class PayoutRequestController {

    private final PayoutRequestService payoutService;

    @GetMapping
    public ResponseEntity<Page<PayoutRequestDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(payoutService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayoutRequestDto> update(@PathVariable UUID id, @RequestBody PayoutRequestDto dto) {
        // Оскільки в useCrud метод save викликає PUT запит і передає все тіло форми,
        // ми просто беремо статус із переданого об'єкта
        return ResponseEntity.ok(payoutService.updateStatus(id, dto.getStatus()));
    }
}