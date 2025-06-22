package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.dto.FundraisingDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.FundraisingListDto;
import com.sakute.project_fumo_backend.domain.service.impl.FundraisingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fundraising")
public class FundraisingController {

    @Autowired
    private FundraisingService fundraisingService;

    @GetMapping
    public ResponseEntity<Page<FundraisingListDto>> getAllFundraisings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "recent") String sortBy,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search) {

        Pageable pageable = createPageable(page, size, sortBy);

        Page<FundraisingListDto> fundraisings = fundraisingService
                .getAllFundraisings(pageable, category, search);

        return ResponseEntity.ok(fundraisings);
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
    public ResponseEntity<Page<FundraisingListDto>> getActiveFundraisings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "recent") String sortBy) {

        Pageable pageable = createPageable(page, size, sortBy);
        Page<FundraisingListDto> fundraisings = fundraisingService.getActiveFundraisings(pageable);

        return ResponseEntity.ok(fundraisings);
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<FundraisingListDto>> getPopularFundraisings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("donorCount").descending());
        Page<FundraisingListDto> fundraisings = fundraisingService.getPopularFundraisings(pageable);

        return ResponseEntity.ok(fundraisings);
    }

    @GetMapping("/ending-soon")
    public ResponseEntity<Page<FundraisingListDto>> getEndingSoonFundraisings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("endDate").ascending());
        Page<FundraisingListDto> fundraisings = fundraisingService.getEndingSoonFundraisings(pageable);

        return ResponseEntity.ok(fundraisings);
    }


    @PostMapping
    public ResponseEntity<FundraisingDto> createFundraising(@Valid @RequestBody FundraisingDto createDto) {
        try {
            FundraisingDto createdFundraising = fundraisingService.createFundraising(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFundraising);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Оновлення існуючого фандрейзингу
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deleteFundraising(@PathVariable UUID id) {
        try {
            fundraisingService.deleteFundraising(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Часткове оновлення фандрейзингу (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<FundraisingDto> partialUpdateFundraising(
            @PathVariable UUID id,
            @RequestBody FundraisingDto updateDto) {
        try {
            FundraisingDto updatedFundraising = fundraisingService.partialUpdateFundraising(id, updateDto);
            return ResponseEntity.ok(updatedFundraising);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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
