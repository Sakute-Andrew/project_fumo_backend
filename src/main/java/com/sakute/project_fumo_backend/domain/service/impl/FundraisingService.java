package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.enteties.dto.FundraisingDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.FundraisingListDto;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.repository.jpa_repo.fundraising.FundraisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class FundraisingService {

    @Autowired
    private FundraisingRepository fundraisingRepository;

    public Page<FundraisingListDto> getAllFundraisings(Pageable pageable, Long category, String search) {
        Page<Fundraising> fundraisings;

        if (category != null && search != null && !search.isEmpty()) {
            // Пошук за категорією та текстом
            fundraisings = fundraisingRepository.findByCategoryAndTitleContainingIgnoreCase(
                    category, search, pageable);
        } else if (category != null) {
            // Тільки за категорією
            fundraisings = fundraisingRepository.findByCategory(category, pageable);
        } else if (search != null && !search.isEmpty()) {
            // Тільки пошук
            fundraisings = fundraisingRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    search, search, pageable);
        } else {
            // Всі фандрейзинги
            fundraisings = fundraisingRepository.findAll(pageable);
        }

        return fundraisings.map(this::mapToListDto);
    }

    public FundraisingDto getFundraisingById(UUID id) {
        Fundraising fundraising = fundraisingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Фандрейзинг не знайдено"));

        return mapToDto(fundraising);
    }

    public Page<FundraisingListDto> getActiveFundraisings(Pageable pageable) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Page<Fundraising> fundraisings = fundraisingRepository.findByEndDateAfter(now, pageable);

        return fundraisings.map(this::mapToListDto);
    }

    public Page<FundraisingListDto> getPopularFundraisings(Pageable pageable) {
        // Припускаємо, що популярність визначається кількістю донатерів
        // Можна додати поле donorCount в Fundraising або використовувати запит
        Page<Fundraising> fundraisings = fundraisingRepository.findAllByOrderByCurrentAmountDesc(pageable);

        return fundraisings.map(this::mapToListDto);
    }

    public Page<FundraisingListDto> getEndingSoonFundraisings(Pageable pageable) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // Фандрейзинги, що закінчуються протягом 7 днів
        Timestamp weekFromNow = new Timestamp(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L);

        Page<Fundraising> fundraisings = fundraisingRepository
                .findByEndDateBetweenOrderByEndDateAsc(now, weekFromNow, pageable);

        return fundraisings.map(this::mapToListDto);
    }

    private FundraisingListDto mapToListDto(Fundraising fundraising) {
        FundraisingListDto dto = new FundraisingListDto();
        dto.setId(fundraising.getId());
        dto.setTitle(fundraising.getTitle());
        dto.setDescription(truncateDescription(fundraising.getDescription(), 150));
        dto.setGoalAmount(fundraising.getGoalAmount());
        dto.setCurrentAmount(fundraising.getCurrentAmount());
        dto.setEndDate(fundraising.getEndDate());
        dto.setCreatedAt(fundraising.getCreatedAt());
        dto.setCategory(fundraising.getCategory());

        // Обчислюємо прогрес
        if (fundraising.getGoalAmount() != null && fundraising.getGoalAmount().intValue() > 0) {
            double progress = fundraising.getCurrentAmount().doubleValue() /
                    fundraising.getGoalAmount().doubleValue() * 100;
            dto.setProgressPercentage((int) Math.min(Math.round(progress), 100));
        } else {
            dto.setProgressPercentage(0);
        }

        // Обчислюємо залишок часу
        dto.setDaysLeft(calculateDaysLeft(fundraising.getEndDate()));

        return dto;
    }

    private FundraisingDto mapToDto(Fundraising fundraising) {
        FundraisingDto dto = new FundraisingDto();
        dto.setId(fundraising.getId());
        dto.setTitle(fundraising.getTitle());
        dto.setDescription(fundraising.getDescription());
        dto.setGoalAmount(fundraising.getGoalAmount());
        dto.setCurrentAmount(fundraising.getCurrentAmount());
        dto.setEndDate(fundraising.getEndDate());
        dto.setCreatedAt(fundraising.getCreatedAt());
        dto.setCategory(fundraising.getCategory());
        dto.setUserName(fundraising.getUserId().getFullName());

        // Додаткова інформація для детальної сторінки
        if (fundraising.getGoalAmount() != null && fundraising.getGoalAmount().intValue() > 0) {
            double progress = fundraising.getCurrentAmount().doubleValue() /
                    fundraising.getGoalAmount().doubleValue() * 100;
            dto.setProgressPercentage((int) Math.min(Math.round(progress), 100));
        } else {
            dto.setProgressPercentage(0);
        }

        dto.setDaysLeft(calculateDaysLeft(fundraising.getEndDate()));
        dto.setActive(fundraising.getEndDate().after(new Timestamp(System.currentTimeMillis())));

        return dto;
    }

    private String truncateDescription(String description, int maxLength) {
        if (description == null || description.length() <= maxLength) {
            return description;
        }
        return description.substring(0, maxLength) + "...";
    }

    private int calculateDaysLeft(Timestamp endDate) {
        if (endDate == null) return 0;

        long diffMs = endDate.getTime() - System.currentTimeMillis();
        if (diffMs <= 0) return 0;

        return (int) (diffMs / (1000 * 60 * 60 * 24));
    }
}
