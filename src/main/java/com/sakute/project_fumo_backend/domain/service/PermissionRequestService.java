package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.PermissionRequestDto;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PermissionRequestService {
    PermissionRequestDto createRequest(String username, PermissionRequestDto dto);
    Page<PermissionRequestDto> getAll(Pageable pageable);
    PermissionRequestDto updateStatus(UUID id, RequestStatus status);
}
