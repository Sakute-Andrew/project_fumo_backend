package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingListDto;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingCategory;
import com.sakute.project_fumo_backend.domain.service.impl.FundraisingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/fundraising")
public class FundraisingController {

    @Autowired
    private FundraisingService fundraisingService;

    @GetMapping
    public ResponseEntity<Page<FundraisingListDto>> getAllFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "recent") String sortBy,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search) {

        Pageable pageable = createPageable(page, size, sortBy);

        Page<FundraisingListDto> fundraisings = fundraisingService
                .getAllFundraising(pageable, category, search);

        return ResponseEntity.ok(fundraisings);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<FundraisingCategory>> getAllCategories() {
        return ResponseEntity.ok(fundraisingService.getCategories());
    }


    @GetMapping("/{id}")
    public ResponseEntity<FundraisingDto> getFundraisingById(@PathVariable UUID id) {
        try {
            FundraisingDto fundraising = fundraisingService.getFundraisingById(id);
            return ResponseEntity.ok(fundraising);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<Page<FundraisingListDto>> getActiveFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "recent") String sortBy) {

        Pageable pageable = createPageable(page, size, sortBy);
        Page<FundraisingListDto> fundraising = fundraisingService.getActiveFundraising(pageable);

        return ResponseEntity.ok(fundraising);
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<FundraisingListDto>> getPopularFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("donorCount").descending());
        Page<FundraisingListDto> fundraising = fundraisingService.getPopularFundraisings(pageable);

        return ResponseEntity.ok(fundraising);
    }

    @GetMapping("/ending-soon")
    public ResponseEntity<Page<FundraisingListDto>> getEndingSoonFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("endDate").ascending());
        Page<FundraisingListDto> fundraising = fundraisingService.getEndingSoonFundraising(pageable);

        return ResponseEntity.ok(fundraising);
    }


    @PostMapping
    public ResponseEntity<FundraisingDto> createFundraising(@Valid @RequestBody FundraisingDto createDto) {
        try {
            FundraisingDto createdFundraising = fundraisingService.createFundraising(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFundraising);
        } catch (RuntimeException e) {
            log.error("Помилка створення фандрейзингу: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Оновлення існуючого фандрейзингу
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @fundraisingService.isFundraisingOwner(#id, authentication.name)")
    public ResponseEntity<FundraisingDto> updateFundraising(
            @PathVariable UUID id,
            @Valid @RequestBody FundraisingDto updateDto) {
        try {
            FundraisingDto updatedFundraising = fundraisingService.updateFundraising(id, updateDto);
            return ResponseEntity.ok(updatedFundraising);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Видалення фандрейзингу
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @fundraisingService.isFundraisingOwner(#id, authentication.name)")
    public ResponseEntity<Void> deleteFundraising(@PathVariable UUID id) {
        try {
            fundraisingService.deleteFundraising(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Часткове оновлення фандрейзингу (PATCH)
    // У твоєму FundraisingController

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @fundraisingService.isFundraisingOwner(#id, authentication.name)")
    public ResponseEntity<FundraisingDto> partialUpdate(
            @PathVariable UUID id,
            @RequestBody FundraisingDto updateDto) { // Тут можна без @Valid, бо поля можуть бути null

        // ВИПРАВЛЕНО: викликаємо універсальний метод updateFundraising
        return ResponseEntity.ok(fundraisingService.updateFundraising(id, updateDto));
    }

    private Pageable createPageable(int page, int size, String sortBy) {
        Sort sort;
        switch (sortBy) {
            case "ending":
                sort = Sort.by("endDate").ascending();
                break;
            case "popular":
                sort = Sort.by("donorCount").descending();
                break;
            case "amount":
                sort = Sort.by("currentAmount").descending();
                break;
            case "recent":
            default:
                sort = Sort.by("createdAt").descending();
                break;
        }
        return PageRequest.of(page, size, sort);
    }
}
