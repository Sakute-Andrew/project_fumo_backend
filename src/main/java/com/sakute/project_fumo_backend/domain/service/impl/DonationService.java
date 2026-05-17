package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.dto.donation.*;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.*;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.DonationRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DonationService {

    private final DonationRepository donationRepository;

    private final FundraisingRepository fundraisingRepository;

    private final UserRepository userRepository;

    public DonationService(DonationRepository donationRepository, FundraisingRepository fundraisingRepository, UserRepository userRepository) {
        this.donationRepository = donationRepository;
        this.fundraisingRepository = fundraisingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DonationResponse processMockDonation(DonationRequest request) {
        try {
            // Валідація
            if (request.getAmount().compareTo(BigDecimal.valueOf(10)) < 0) {
                return new DonationResponse(false, "Мінімальна сума донату 10 грн", null);
            }

            if (request.getAmount().compareTo(BigDecimal.valueOf(50000)) > 0) {
                return new DonationResponse(false, "Максимальна сума донату 50000 грн", null);
            }

            // Перевірка існування фандрейзингу
            Fundraising fundraising = fundraisingRepository.findById(request.getFundraisingId())
                    .orElseThrow(() -> new RuntimeException("Фандрейзинг не знайдено"));

            // Перевірка чи не закінчився фандрейзинг
            if (fundraising.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
                return new DonationResponse(false, "Термін збору коштів завершено", null);
            }

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("DEBUG: User from token is -> {}", username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("User Not Found"));


            // Імітація обробки платежу
            Thread.sleep(1500); // Імітація затримки платіжної системи

            // Створення донату
            Donation donation = new Donation();
            donation.setFundraising(fundraising);
            donation.setAmount(request.getAmount());
            donation.setDonor(user);
            donation.setTransactionId("MOCK_" + System.currentTimeMillis());
            donation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            donation.setIsAnonymous(request.getIsAnonymous());// Або інший статус, який очікує твоя база

            // Збереження донату
            donation = donationRepository.save(donation);

            fundraisingRepository.save(fundraising);

            return new DonationResponse(
                    true,
                    "Донат успішно оброблено! Дякуємо за підтримку!",
                    donation.getTransactionId()
            );

        } catch (InterruptedException e) {
            return new DonationResponse(false, "Помилка обробки платежу", null);
        } catch (Exception e) {
            return new DonationResponse(false, "Внутрішня помилка сервера", null);
        }
    }

    @Transactional(readOnly = true)
    public Page<DonationListDto> getAllDonationsForAdmin(UUID fundraisingId, Pageable pageable) {
        Page<Donation> donations;

        // Якщо передали ID збору (натиснули кнопку "Донати" в адмінці) - фільтруємо
        if (fundraisingId != null) {
            donations = donationRepository.findByFundraising_Id(fundraisingId, pageable);
        } else {
            // Інакше показуємо взагалі всі донати
            donations = donationRepository.findAll(pageable);
        }

        // Конвертуємо Entity -> DTO
        return donations.map(donation -> {
            // Визначаємо ім'я донатера
            String donorName = "Невідомо";
            if (donation.getDonor() != null) {
                // Можна брати fullName або username
                donorName = donation.getDonor().getUsername();
            }

            // Визначаємо назву збору
            String fundraisingTitle = "Видалений збір";
            if (donation.getFundraising() != null) {
                fundraisingTitle = donation.getFundraising().getTitle();
            }

            return new DonationListDto(
                    donation.getId(),
                    donation.getAmount(),
                    donation.getTransactionId(),
                    donation.getCreatedAt(),
                    donation.getIsAnonymous(),
                    donorName,
                    fundraisingTitle
            );
        });
    }

    @Transactional
    public void deleteDonation(UUID donationId) throws NotFoundException {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException("Донат не знайдено"));

        // Опціонально: якщо ви видаляєте донат, можливо, треба відняти суму від currentAmount збору
        // Fundraising fundraising = donation.getFundraising();
        // fundraising.setCurrentAmount(fundraising.getCurrentAmount().subtract(donation.getAmount()));
        // fundraisingRepository.save(fundraising);

        donationRepository.delete(donation);
    }

    @Transactional(readOnly = true)
    public DonationStatsDto getDonationStats(UUID fundraisingId) {
        List<Donation> successfulDonations = donationRepository.findByFundraising_Id(fundraisingId);

        BigDecimal totalAmount = successfulDonations.stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDonors = successfulDonations.size();

        BigDecimal averageDonation = totalDonors > 0
                ? totalAmount.divide(BigDecimal.valueOf(totalDonors), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal largestDonation = successfulDonations.stream()
                .map(Donation::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return new DonationStatsDto(totalAmount, totalDonors, averageDonation, largestDonation);
    }

    private DonorDisplayDto mapToDonorDisplay(Donation donation) {
        String displayName = donation.getIsAnonymous() || donation.getDonor().getFullName() == null
                ? "Анонім"
                : maskName(donation.getDonor().getFullName());

        return new DonorDisplayDto(
                displayName,
                donation.getAmount(),
                donation.getCreatedAt(),
                donation.getIsAnonymous()
        );
    }

    @Transactional(readOnly = true)
    public List<DonorDisplayDto> getDonors(UUID fundraisingId) {
        return donationRepository.findByFundraising_IdOrderByCreatedAtDesc(fundraisingId)
                .stream()
                .map(this::mapToDonorDisplay)
                .collect(Collectors.toList());
    }

    public List<DonorDisplayDto> getTopDonors(UUID fundraisingId, int limit) {
        return donationRepository.findByFundraising_IdOrderByCreatedAtDesc(fundraisingId)
                .stream()
                .sorted(Comparator.comparing(Donation::getAmount).reversed())
                .limit(limit)
                .map(this::mapToDonorDisplay)
                .collect(Collectors.toList());
    }

    private String maskName(String name) {
        if (name == null || name.length() <= 2) return "Анонім";
        return name.charAt(0) + "***" + name.charAt(name.length() - 1);
    }



}
