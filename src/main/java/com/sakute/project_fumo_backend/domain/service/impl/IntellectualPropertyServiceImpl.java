package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exeption.NotAuthorizedExeption;
import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import com.sakute.project_fumo_backend.controller.exeption.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.enteties.dto.IntellectualPropertyDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.mapper.IntellectualPropertyMapper;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.intprop.IntellectualPropertyCategoryRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.intprop.IntellectualPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class IntellectualPropertyServiceImpl {

    private final IntellectualPropertyRepository intellectualPropertyRepository;
    private final IntellectualPropertyCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final IntellectualPropertyMapper mapper;

    @Transactional(readOnly = true)
    public Page<IntellectualPropertyDto> findAll(Pageable pageable) {
        Page<IntellectualProperty> entities = intellectualPropertyRepository.findAll(pageable);
        return entities.map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public IntellectualPropertyDto findById(UUID id) {
        IntellectualProperty entity = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intellectual Property not found with id: " + id));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<IntellectualPropertyDto> findByName(String name) {
        List<IntellectualProperty> entities = intellectualPropertyRepository.findByNameContainingIgnoreCase(name);
        return entities.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<IntellectualPropertyDto> findByCategory(Long categoryId) {
        List<IntellectualProperty> entities = intellectualPropertyRepository.findByIntellectualPropertyCategory_CategoryId(categoryId);
        return entities.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<IntellectualPropertyDto> findByCurrentUser() {
        String currentUsername = getCurrentUsername();
        List<IntellectualProperty> entities = intellectualPropertyRepository.findByOwner_Username(currentUsername);
        return entities.stream().map(mapper::toDto).toList();
    }

    public IntellectualPropertyDto create(IntellectualPropertyDto dto) {
        IntellectualProperty entity = mapper.toEntity(dto);

        // Встановлюємо поточного користувача як власника
        String currentUsername = getCurrentUsername();
        User owner = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        entity.setOwner(owner);

        // Встановлюємо час створення та початковий статус
        entity.setCreatedAt(Timestamp.from(Instant.now()));
        entity.setStatus(IpStatus.AVAILABLE);

        IntellectualProperty saved = intellectualPropertyRepository.save(entity);
        return mapper.toDto(saved);
    }

    public IntellectualPropertyDto update(UUID id, IntellectualPropertyDto dto) {
        IntellectualProperty existing = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intellectual Property not found with id: " + id));

        // Перевіряємо права доступу
        if (!isOwner(id) && !isAdmin()) {
            throw new OperationNotAllowedException();
        }

        // Оновлюємо поля
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setTypeIp(dto.getTypeIp());
        existing.setFileIp(dto.getFileIp());

        IntellectualProperty updated = intellectualPropertyRepository.save(existing);
        return mapper.toDto(updated);
    }

    public void delete(UUID id) {
        IntellectualProperty entity = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Intellectual Property not found with id: " + id));

        if (!isOwner(id) && !isAdmin()) {
            throw new NotAuthorizedExeption("You don't have permission to delete this intellectual property");
        }

        intellectualPropertyRepository.delete(entity);
    }

    public IntellectualPropertyDto changeStatus(UUID id, String statusStr) {
        IntellectualProperty entity = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intellectual Property not found with id: " + id));

        IpStatus status = IpStatus.valueOf(statusStr.toUpperCase());
        entity.setStatus(status);

        IntellectualProperty updated = intellectualPropertyRepository.save(entity);
        return mapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(UUID id) {
        String currentUsername = getCurrentUsername();
        return intellectualPropertyRepository.existsByIpIdAndOwner_Username(id, currentUsername);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
