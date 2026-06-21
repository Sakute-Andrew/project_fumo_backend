package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.PermissionRequestDto;
import com.sakute.project_fumo_backend.domain.service.PermissionRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/permission-requests")
@RequiredArgsConstructor
public class PermissionRequestController {

    private final PermissionRequestService requestService;

    // Адмін отримує список всіх заявок
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PermissionRequestDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(requestService.getAll(pageable));
    }

    // Адмін змінює статус (схвалює або відхиляє)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionRequestDto> updateStatus(
            @PathVariable UUID id,
            @RequestBody PermissionRequestDto dto) {
        return ResponseEntity.ok(requestService.updateStatus(id, dto.getStatus()));
    }

    // Звичайний користувач створює заявку (наприклад, з налаштувань профілю)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PermissionRequestDto> createRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PermissionRequestDto dto) {
        return ResponseEntity.ok(requestService.createRequest(userDetails.getUsername(), dto));
    }

    // Користувач переглядає свої заявки (для відображення статусу на фронтенді)
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PermissionRequestDto>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(requestService.getMyRequests(userDetails.getUsername()));
    }
}