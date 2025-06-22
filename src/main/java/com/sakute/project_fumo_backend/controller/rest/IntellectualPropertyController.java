package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.dto.IntellectualPropertyDto;
import com.sakute.project_fumo_backend.domain.service.impl.IntellectualPropertyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/intprop")
@RequiredArgsConstructor
@Validated
public class IntellectualPropertyController {

    private final IntellectualPropertyServiceImpl intellectualPropertyService;

    // Отримати список всіх IP з пагінацією
    @GetMapping
    public ResponseEntity<Page<IntellectualPropertyDto>> getIntpropList(Pageable pageable) {
        Page<IntellectualPropertyDto> result = intellectualPropertyService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // Отримати IP за ID
    @GetMapping("/{id}")
    public ResponseEntity<IntellectualPropertyDto> getIntpropById(@PathVariable UUID id) {
        IntellectualPropertyDto result = intellectualPropertyService.findById(id);
        return ResponseEntity.ok(result);
    }

    // Пошук IP за назвою
    @GetMapping("/search")
    public ResponseEntity<List<IntellectualPropertyDto>> searchByName(
            @RequestParam("name") String name) {
        List<IntellectualPropertyDto> result = intellectualPropertyService.findByName(name);
        return ResponseEntity.ok(result);
    }

    // Отримати IP за категорією
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<IntellectualPropertyDto>> getByCategory(
            @PathVariable Long categoryId) {
        List<IntellectualPropertyDto> result = intellectualPropertyService.findByCategory(categoryId);
        return ResponseEntity.ok(result);
    }

    // Отримати IP поточного користувача
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<IntellectualPropertyDto>> getMyIntprop() {
        List<IntellectualPropertyDto> result = intellectualPropertyService.findByCurrentUser();
        return ResponseEntity.ok(result);
    }

    // Створити нову IP
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IntellectualPropertyDto> create(@Valid @RequestBody IntellectualPropertyDto dto) {
        IntellectualPropertyDto result = intellectualPropertyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // Оновити IP
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @intellectualPropertyServiceImpl.isOwner(#id)")
    public ResponseEntity<IntellectualPropertyDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody IntellectualPropertyDto dto) {
        IntellectualPropertyDto result = intellectualPropertyService.update(id, dto);
        return ResponseEntity.ok(result);
    }

    // Видалити IP
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @intellectualPropertyServiceImpl.isOwner(#id)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        intellectualPropertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Змінити статус IP (для адмінів)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IntellectualPropertyDto> changeStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        IntellectualPropertyDto result = intellectualPropertyService.changeStatus(id, status);
        return ResponseEntity.ok(result);
    }
}
