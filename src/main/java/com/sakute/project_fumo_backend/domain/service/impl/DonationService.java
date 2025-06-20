package com.sakute.project_fumo_backend.domain.service.impl;


import com.sakute.project_fumo_backend.domain.enteties.dto.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonationRequest;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonationResponse;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonationStatsDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.donation.DonorDisplayDto;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.*;
import com.sakute.project_fumo_backend.repository.DonationRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.fundraising.FundraisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private FundraisingRepository fundraisingRepository;

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

            // Імітація обробки платежу
            Thread.sleep(1500); // Імітація затримки платіжної системи

            // Створення донату
            Donation donation = new Donation();
            donation.setFundraisingId(request.getFundraisingId());
            donation.setAmount(request.getAmount());
            donation.setDonorName(request.getDonorName());
            donation.setDonorEmail(request.getDonorEmail());
            donation.setTransactionId("MOCK_" + System.currentTimeMillis());
            donation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            donation.setIsAnonymous(request.getIsAnonymous());

            // Збереження донату
            donation = donationRepository.save(donation);

            // Оновлення поточної суми у фандрейзингу
            fundraising.setCurrentAmount(
                    fundraising.getCurrentAmount().add(request.getAmount())
            );
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


    public DonationStatsDto getDonationStats(UUID fundraisingId) {
        List<Donation> successfulDonations = donationRepository.findByFundraisingId(fundraisingId);

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
        String displayName = donation.getIsAnonymous() || donation.getDonorName() == null
                ? "Анонім"
                : maskName(donation.getDonorName());

        return new DonorDisplayDto(
                displayName,
                donation.getAmount(),
                donation.getCreatedAt(),
                donation.getIsAnonymous()
        );
    }

    public List<DonorDisplayDto> getDonors(UUID fundraisingId) {
        return null;
        //+return donationRepository.findTopByDonorFundraisingId(fundraisingId);
    }

    private String maskName(String name) {
        if (name == null || name.length() <= 2) return "Анонім";
        return name.charAt(0) + "***" + name.charAt(name.length() - 1);
    }



}
