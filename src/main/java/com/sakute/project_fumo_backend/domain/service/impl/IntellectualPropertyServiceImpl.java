package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.service.IntellectualPropertyService;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyCategoryDto;
import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyDto;
import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyMapper;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualPropertyCategory;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.specification.IntellectualPropertySpec;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.IntellectualPropertyCategoryRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.IntellectualPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
public class IntellectualPropertyServiceImpl implements IntellectualPropertyService {

    private final IntellectualPropertyRepository intellectualPropertyRepository;
    private final IntellectualPropertyCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final IntellectualPropertyMapper mapper;

    // Додай новий метод findAll з фільтрами
    @Transactional(readOnly = true)
    public Page<IntellectualPropertyDto> findAll(
            Long categoryId,
            String status,
            String name,
            Pageable pageable) {

        Specification<IntellectualProperty> spec = Specification
                .where(IntellectualPropertySpec.hasCategory(categoryId))
                .and(IntellectualPropertySpec.hasStatus(status))
                .and(IntellectualPropertySpec.nameContains(name));

        return intellectualPropertyRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    // Старий метод залиш або зроби делегування
    @Transactional(readOnly = true)
    public Page<IntellectualPropertyDto> findAll(Pageable pageable) {
        return findAll(null, null, null, pageable);
    }

    @Transactional(readOnly = true)
    public IntellectualPropertyDto findById(UUID id) {
        IntellectualProperty entity = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Об'єкт інтелектуальної власності не знайдено: " + id));
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

    public List<IntellectualPropertyCategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new IntellectualPropertyCategoryDto(c.getCategoryId(), c.getCategoryName()))
                .toList();
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
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено: " + currentUsername));
        entity.setOwner(owner);

        // Встановлюємо час створення та початковий статус
        entity.setCreatedAt(Timestamp.from(Instant.now()));
        entity.setStatus(IpStatus.AVAILABLE);

        IntellectualProperty saved = intellectualPropertyRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional
    public IntellectualPropertyCategoryDto createCategory(IntellectualPropertyCategoryDto dto) {
        IntellectualPropertyCategory category = new IntellectualPropertyCategory();
        category.setCategoryId(dto.getCategoryId());
        category.setCategoryName(dto.getCategoryName());
        IntellectualPropertyCategory saved = categoryRepository.save(category);
        return new IntellectualPropertyCategoryDto(saved.getCategoryId(), saved.getCategoryName());
    }

    @Transactional
    public void deleteCategory(long id){
        categoryRepository.deleteById(id);
    }

    public IntellectualPropertyDto update(UUID id, IntellectualPropertyDto dto) {
        IntellectualProperty existing = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Об'єкт інтелектуальної власності не знайдено: " + id));

        // Перевіряємо права доступу
        if (!isOwner(id) && !isAdmin()) {
            throw new OperationNotAllowedException("У вас немає доступу!");
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
                .orElseThrow(() -> new NotFoundException("Об'єкт інтелектуальної власності не знайдено: " + id));

        if (!isOwner(id) && !isAdmin()) {
            throw new OperationNotAllowedException("У вас немає прав для видалення цього об'єкта інтелектуальної власності");
        }

        intellectualPropertyRepository.delete(entity);
    }

    public IntellectualPropertyDto changeStatus(UUID id, String statusStr) {
        IntellectualProperty entity = intellectualPropertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Об'єкт інтелектуальної власності не знайдено: " + id));

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
