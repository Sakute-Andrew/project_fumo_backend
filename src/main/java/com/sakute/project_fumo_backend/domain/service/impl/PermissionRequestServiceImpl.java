package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.service.PermissionRequestService;
import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;

import com.sakute.project_fumo_backend.domain.dto.PermissionRequestDto;
import com.sakute.project_fumo_backend.domain.enteties.PermissionRequest;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.PermissionRequestRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionRequestServiceImpl implements PermissionRequestService {

    private final PermissionRequestRepository requestRepository;
    private final UserRepository userRepository;

    // Створення заявки КОРИСТУВАЧЕМ
    @Transactional
    public PermissionRequestDto createRequest(String username, PermissionRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено"));

        // Якщо юзер вже має це право — відмова
        if (user.getPermissions().contains(dto.getRequestedPermission())) {
            throw new InvalidInputException("Ви вже маєте цей дозвіл");
        }

        // Якщо вже є заявка в очікуванні — відмова
        if (requestRepository.existsByUser_UserIdAndRequestedPermissionAndStatus(user.getUserId(), dto.getRequestedPermission(), RequestStatus.PENDING)) {
            throw new InvalidInputException("Ваша попередня заявка ще розглядається");
        }

        PermissionRequest request = new PermissionRequest();
        request.setUser(user);
        request.setRequestedPermission(dto.getRequestedPermission());
        request.setMessage(dto.getMessage());
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return toDto(requestRepository.save(request));
    }

    // Отримання всіх заявок для АДМІН-ПАНЕЛІ
    @Transactional(readOnly = true)
    public Page<PermissionRequestDto> getAll(Pageable pageable) {
        return requestRepository.findAll(pageable).map(this::toDto);
    }

    // Обробка заявки АДМІНІСТРАТОРОМ
    @Transactional
    public PermissionRequestDto updateStatus(UUID id, RequestStatus status) {
        PermissionRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявку не знайдено"));

        request.setStatus(status);

        User user = userRepository.findById(request.getUser().getUserId())
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено"));

        if (RequestStatus.APPROVED.equals(status)) {
            user.getPermissions().add(request.getRequestedPermission());
            userRepository.save(user);
        }

        requestRepository.save(request);
        return toDtoWithUser(request, user); // передаємо вже завантаженого юзера
    }

    private PermissionRequestDto toDto(PermissionRequest entity) {
        PermissionRequestDto dto = new PermissionRequestDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setUsername(entity.getUser().getUsername());
        dto.setRequestedPermission(entity.getRequestedPermission());
        dto.setStatus(entity.getStatus());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private PermissionRequestDto toDtoWithUser(PermissionRequest entity, User user) {
        PermissionRequestDto dto = new PermissionRequestDto();
        dto.setId(entity.getId());
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRequestedPermission(entity.getRequestedPermission());
        dto.setStatus(entity.getStatus());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}