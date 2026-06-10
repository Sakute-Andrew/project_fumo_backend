package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingCategoryDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingListDto;
import com.sakute.project_fumo_backend.domain.service.FundraisingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class FundraisingController {

    private final FundraisingService fundraisingService;

    @GetMapping
    public ResponseEntity<Page<FundraisingListDto>> getAllFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "recent") String sortBy,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search) {

        Pageable pageable = createPageable(page, size, sortBy);
        return ResponseEntity.ok(fundraisingService.getAllFundraising(pageable, category, search));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<FundraisingCategoryDto>> getAllCategories() {
        return ResponseEntity.ok(fundraisingService.getCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundraisingDto> getFundraisingById(@PathVariable UUID id) {
        return ResponseEntity.ok(fundraisingService.getFundraisingById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<FundraisingListDto>> getActiveFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "recent") String sortBy) {

        Pageable pageable = createPageable(page, size, sortBy);
        return ResponseEntity.ok(fundraisingService.getActiveFundraising(pageable));
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<FundraisingListDto>> getPopularFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("donorCount").descending());
        return ResponseEntity.ok(fundraisingService.getPopularFundraisings(pageable));
    }

    @GetMapping("/ending-soon")
    public ResponseEntity<Page<FundraisingListDto>> getEndingSoonFundraising(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("endDate").ascending());
        return ResponseEntity.ok(fundraisingService.getEndingSoonFundraising(pageable));
    }

    @PostMapping
    public ResponseEntity<FundraisingDto> createFundraising(@Valid @RequestBody FundraisingDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fundraisingService.createFundraising(createDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @fundraisingServiceImpl.isFundraisingOwner(#id, authentication.name)")
    public ResponseEntity<FundraisingDto> updateFundraising(
            @PathVariable UUID id,
            @Valid @RequestBody FundraisingDto updateDto) {
        return ResponseEntity.ok(fundraisingService.updateFundraising(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @fundraisingServiceImpl.isFundraisingOwner(#id, authentication.name)")
    public ResponseEntity<Void> deleteFundraising(@PathVariable UUID id) {
        fundraisingService.deleteFundraising(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @fundraisingServiceImpl.isFundraisingOwner(#id, authentication.name)")
    public ResponseEntity<FundraisingDto> partialUpdate(
            @PathVariable UUID id,
            @RequestBody FundraisingDto updateDto) {
        return ResponseEntity.ok(fundraisingService.updateFundraising(id, updateDto));
    }

    private Pageable createPageable(int page, int size, String sortBy) {
        Sort sort = switch (sortBy) {
            case "ending" -> Sort.by("endDate").ascending();
            case "popular" -> Sort.by("donorCount").descending();
            case "amount" -> Sort.by("currentAmount").descending();
            default -> Sort.by("createdAt").descending();
        };
        return PageRequest.of(page, size, sort);
    }
}
