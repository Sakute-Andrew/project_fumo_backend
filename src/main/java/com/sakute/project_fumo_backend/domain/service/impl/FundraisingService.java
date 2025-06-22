package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.enteties.dto.FundraisingDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.FundraisingListDto;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.repository.jpa_repo.fundraising.FundraisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Service
public class FundraisingService {

    @Autowired
    private FundraisingRepository fundraisingRepository;

    public Page<FundraisingListDto> getAllFundraisings(Pageable pageable, Long category, String search) {
        Page<Fundraising> fundraisings;

        if (category != null && search != null && !search.isEmpty()) {
            fundraisings = fundraisingRepository.findByCategoryAndTitleContainingIgnoreCase(
                    category, search, pageable);
        } else if (category != null) {
            fundraisings = fundraisingRepository.findByCategory(category, pageable);
        } else if (search != null && !search.isEmpty()) {
            fundraisings = fundraisingRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    search, search, pageable);
        } else {
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
        Page<Fundraising> fundraisings = fundraisingRepository.findAllByOrderByCurrentAmountDesc(pageable);

        return fundraisings.map(this::mapToListDto);
    }

    public Page<FundraisingListDto> getEndingSoonFundraisings(Pageable pageable) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
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

    // Додати ці методи до FundraisingService

    // Створення нового фандрейзингу з FundraisingDto
    public FundraisingDto createFundraising(FundraisingDto createDto) {
        // Валідація обов'язкових полів
        if (createDto.getTitle() == null || createDto.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Назва фандрейзингу обов'язкова");
        }
        if (createDto.getDescription() == null || createDto.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Опис фандрейзингу обов'язковий");
        }
        if (createDto.getGoalAmount() == null || createDto.getGoalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Цільова сума повинна бути більше нуля");
        }
        if (createDto.getEndDate() == null) {
            throw new RuntimeException("Дата закінчення обов'язкова");
        }
        if (createDto.getCategory() == null) {
            throw new RuntimeException("Категорія обов'язкова");
        }

        // Валідація дати закінчення
        if (createDto.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("Дата закінчення не може бути в минулому");
        }

        Fundraising fundraising = new Fundraising();
        fundraising.setId(UUID.randomUUID());
        fundraising.setTitle(createDto.getTitle().trim());
        fundraising.setDescription(createDto.getDescription().trim());
        fundraising.setGoalAmount(createDto.getGoalAmount());
        fundraising.setCurrentAmount(BigDecimal.ZERO);
        fundraising.setEndDate(createDto.getEndDate());
        fundraising.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        fundraising.setCategory(createDto.getCategory());

        // TODO: Потрібно отримати користувача з контексту безпеки
        // fundraising.setUserId(getCurrentUser());

        Fundraising savedFundraising = fundraisingRepository.save(fundraising);
        return mapToDto(savedFundraising);
    }

    // Повне оновлення фандрейзингу з FundraisingDto
    public FundraisingDto updateFundraising(UUID id, FundraisingDto updateDto) {
        Fundraising fundraising = fundraisingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Фандрейзинг не знайдено"));

        // TODO: Перевірка прав доступу
        // if (!fundraising.getUserId().equals(getCurrentUser())) {
        //     throw new RuntimeException("Немає прав для редагування");
        // }

        // Перевірка чи фандрейзинг активний
        if (fundraising.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("Неможливо редагувати завершений фандрейзинг");
        }

        // Валідація та оновлення полів
        if (updateDto.getTitle() != null) {
            if (updateDto.getTitle().trim().isEmpty()) {
                throw new RuntimeException("Назва не може бути порожньою");
            }
            fundraising.setTitle(updateDto.getTitle().trim());
        }

        if (updateDto.getDescription() != null) {
            if (updateDto.getDescription().trim().isEmpty()) {
                throw new RuntimeException("Опис не може бути порожнім");
            }
            fundraising.setDescription(updateDto.getDescription().trim());
        }

        if (updateDto.getGoalAmount() != null) {
            if (updateDto.getGoalAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Цільова сума повинна бути більше нуля");
            }
            // Перевірка що нова цільова сума не менша за поточну зібрану суму
            if (updateDto.getGoalAmount().compareTo(fundraising.getCurrentAmount()) < 0) {
                throw new RuntimeException("Цільова сума не може бути меншою за вже зібрану суму");
            }
            fundraising.setGoalAmount(updateDto.getGoalAmount());
        }

        if (updateDto.getEndDate() != null) {
            // Перевірка що нова дата не в минулому
            if (updateDto.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
                throw new RuntimeException("Дата закінчення не може бути в минулому");
            }
            fundraising.setEndDate(updateDto.getEndDate());
        }

        if (updateDto.getCategory() != null) {
            fundraising.setCategory(updateDto.getCategory());
        }

        Fundraising updatedFundraising = fundraisingRepository.save(fundraising);
        return mapToDto(updatedFundraising);
    }

    // Часткове оновлення фандрейзингу з FundraisingDto
    public FundraisingDto partialUpdateFundraising(UUID id, FundraisingDto updateDto) {
        Fundraising fundraising = fundraisingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Фандрейзинг не знайдено"));

        // TODO: Перевірка прав доступу
        // if (!fundraising.getUserId().equals(getCurrentUser())) {
        //     throw new RuntimeException("Немає прав для редагування");
        // }

        // Перевірка чи фандрейзинг активний
        if (fundraising.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
            throw new RuntimeException("Неможливо редагувати завершений фандрейзинг");
        }

        // Часткове оновлення - оновлюємо тільки передані поля
        boolean wasUpdated = false;

        if (updateDto.getTitle() != null && !updateDto.getTitle().trim().isEmpty()) {
            fundraising.setTitle(updateDto.getTitle().trim());
            wasUpdated = true;
        }

        if (updateDto.getDescription() != null && !updateDto.getDescription().trim().isEmpty()) {
            fundraising.setDescription(updateDto.getDescription().trim());
            wasUpdated = true;
        }

        if (updateDto.getGoalAmount() != null) {
            if (updateDto.getGoalAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Цільова сума повинна бути більше нуля");
            }
            if (updateDto.getGoalAmount().compareTo(fundraising.getCurrentAmount()) < 0) {
                throw new RuntimeException("Цільова сума не може бути меншою за вже зібрану суму");
            }
            fundraising.setGoalAmount(updateDto.getGoalAmount());
            wasUpdated = true;
        }

        if (updateDto.getEndDate() != null) {
            if (updateDto.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
                throw new RuntimeException("Дата закінчення не може бути в минулому");
            }
            fundraising.setEndDate(updateDto.getEndDate());
            wasUpdated = true;
        }

        if (updateDto.getCategory() != null) {
            fundraising.setCategory(updateDto.getCategory());
            wasUpdated = true;
        }

        if (!wasUpdated) {
            throw new RuntimeException("Немає даних для оновлення");
        }

        Fundraising updatedFundraising = fundraisingRepository.save(fundraising);
        return mapToDto(updatedFundraising);
    }

    // Видалення фандрейзингу
    public void deleteFundraising(UUID id) {
        Fundraising fundraising = fundraisingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Фандрейзинг не знайдено"));

        // TODO: Перевірка прав доступу
        // if (!fundraising.getUserId().equals(getCurrentUser())) {
        //     throw new RuntimeException("Немає прав для видалення");
        // }

        // Перевірка чи є донати - якщо є, заборонити видалення
        if (fundraising.getCurrentAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Неможливо видалити фандрейзинг з донатами. Використайте деактивацію.");
        }

        // Додаткова перевірка - чи є пов'язані записи (наприклад, коментарі, лайки тощо)
        // if (hasRelatedData(id)) {
        //     throw new RuntimeException("Неможливо видалити фандрейзинг з пов'язаними даними");
        // }

        fundraisingRepository.delete(fundraising);
    }
}
