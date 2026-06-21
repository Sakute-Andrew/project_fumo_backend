package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.dto.donation.DonationRequest;
import com.sakute.project_fumo_backend.domain.dto.donation.DonationResponse;
import com.sakute.project_fumo_backend.domain.dto.donation.DonationStatsDto;
import com.sakute.project_fumo_backend.domain.dto.donation.DonorDisplayDto;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.email.EmailService;
import com.sakute.project_fumo_backend.repository.jpa_repo.DonationRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceImplTest {

    @Mock private DonationRepository donationRepository;
    @Mock private FundraisingRepository fundraisingRepository;
    @Mock private UserRepository userRepository;
    @Mock private EmailService emailService;

    private DonationServiceImpl donationService;

    @BeforeEach
    void setUp() {
        donationService = new DonationServiceImpl(donationRepository, fundraisingRepository, userRepository, emailService);
    }

    // -------------------------------------------------------
    // processMockDonation — validation paths (no Thread.sleep)
    // -------------------------------------------------------

    @Test
    void processMockDonation_shouldFail_whenAmountBelowMinimum() {
        DonationResponse response = donationService.processMockDonation(request(new BigDecimal("5.00")));

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("10");
        verifyNoInteractions(fundraisingRepository);
    }

    @Test
    void processMockDonation_shouldFail_whenAmountAboveMaximum() {
        DonationResponse response = donationService.processMockDonation(request(new BigDecimal("60000.00")));

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("50000");
        verifyNoInteractions(fundraisingRepository);
    }

    @Test
    void processMockDonation_shouldFail_whenFundraisingNotFound() {
        UUID id = UUID.randomUUID();
        when(fundraisingRepository.findById(id)).thenReturn(Optional.empty());

        DonationResponse response = donationService.processMockDonation(request(new BigDecimal("100.00"), id));

        assertThat(response.isSuccess()).isFalse();
    }

    @Test
    void processMockDonation_shouldFail_whenFundraisingExpired() {
        UUID id = UUID.randomUUID();
        Fundraising expired = new Fundraising();
        expired.setEndDate(Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(expired));

        DonationResponse response = donationService.processMockDonation(request(new BigDecimal("100.00"), id));

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("завершено");
    }

    // -------------------------------------------------------
    // getDonationStats
    // -------------------------------------------------------

    @Test
    void getDonationStats_shouldReturnAllZeros_whenNoDonations() {
        UUID id = UUID.randomUUID();
        when(donationRepository.findByFundraising_Id(id)).thenReturn(List.of());

        DonationStatsDto stats = donationService.getDonationStats(id);

        assertThat(stats.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(stats.getTotalDonors()).isZero();
        assertThat(stats.getAverageDonation()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(stats.getLargestDonation()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getDonationStats_shouldCalculateCorrectly_withMultipleDonations() {
        UUID id = UUID.randomUUID();
        when(donationRepository.findByFundraising_Id(id)).thenReturn(List.of(
                donation(new BigDecimal("100.00")),
                donation(new BigDecimal("200.00")),
                donation(new BigDecimal("300.00"))
        ));

        DonationStatsDto stats = donationService.getDonationStats(id);

        assertThat(stats.getTotalAmount()).isEqualByComparingTo(new BigDecimal("600.00"));
        assertThat(stats.getTotalDonors()).isEqualTo(3);
        assertThat(stats.getAverageDonation()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(stats.getLargestDonation()).isEqualByComparingTo(new BigDecimal("300.00"));
    }

    @Test
    void getDonationStats_shouldHandleSingleDonation() {
        UUID id = UUID.randomUUID();
        when(donationRepository.findByFundraising_Id(id)).thenReturn(List.of(
                donation(new BigDecimal("450.00"))
        ));

        DonationStatsDto stats = donationService.getDonationStats(id);

        assertThat(stats.getTotalDonors()).isEqualTo(1);
        assertThat(stats.getAverageDonation()).isEqualByComparingTo(new BigDecimal("450.00"));
        assertThat(stats.getLargestDonation()).isEqualByComparingTo(new BigDecimal("450.00"));
    }

    // -------------------------------------------------------
    // getDonors — name masking and anonymity
    // -------------------------------------------------------

    @Test
    void getDonors_shouldShowFullName_forNonAnonymousDonor() {
        UUID id = UUID.randomUUID();
        when(donationRepository.findByFundraising_IdOrderByCreatedAtDesc(id))
                .thenReturn(List.of(donationWithUser(false)));

        List<DonorDisplayDto> donors = donationService.getDonors(id);

        assertThat(donors).hasSize(1);
        assertThat(donors.getFirst().getDisplayName()).isEqualTo("Andrii Doe");
    }

    @Test
    void getDonors_shouldReturnAnonim_forAnonymousDonor() {
        UUID id = UUID.randomUUID();
        when(donationRepository.findByFundraising_IdOrderByCreatedAtDesc(id))
                .thenReturn(List.of(donationWithUser(true)));

        List<DonorDisplayDto> donors = donationService.getDonors(id);

        assertThat(donors.getFirst().getDisplayName()).isEqualTo("Анонім");
    }

    @Test
    void getDonors_shouldFallbackToUsername_whenFullNameIsNull() {
        UUID id = UUID.randomUUID();
        Donation d = donation(new BigDecimal("100.00"));
        User u = new User();
        u.setUsername("andrii_doe");
        d.setDonor(u);
        d.setIsAnonymous(false);
        when(donationRepository.findByFundraising_IdOrderByCreatedAtDesc(id)).thenReturn(List.of(d));

        List<DonorDisplayDto> donors = donationService.getDonors(id);

        assertThat(donors.getFirst().getDisplayName()).isEqualTo("andrii_doe");
    }

    @Test
    void getDonors_shouldReturnAnonim_whenDonorIsNull() {
        UUID id = UUID.randomUUID();
        Donation d = donation(new BigDecimal("100.00"));
        d.setDonor(null);
        d.setIsAnonymous(false);
        when(donationRepository.findByFundraising_IdOrderByCreatedAtDesc(id)).thenReturn(List.of(d));

        List<DonorDisplayDto> donors = donationService.getDonors(id);

        assertThat(donors.getFirst().getDisplayName()).isEqualTo("Анонім");
    }

    // -------------------------------------------------------
    // getTopDonors — sorting and limiting
    // -------------------------------------------------------

    @Test
    void getTopDonors_shouldReturnTopNSortedByAmountDesc() {
        UUID id = UUID.randomUUID();
        User user = user("Test Person");

        when(donationRepository.findByFundraising_IdOrderByCreatedAtDesc(id)).thenReturn(List.of(
                donationWithUser(new BigDecimal("100.00"), user),
                donationWithUser(new BigDecimal("500.00"), user),
                donationWithUser(new BigDecimal("200.00"), user)
        ));

        List<DonorDisplayDto> top2 = donationService.getTopDonors(id, 2);

        assertThat(top2).hasSize(2);
        assertThat(top2.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(top2.get(1).getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void getTopDonors_shouldReturnAll_whenLimitExceedsSize() {
        UUID id = UUID.randomUUID();
        User user = user("Test Person");

        when(donationRepository.findByFundraising_IdOrderByCreatedAtDesc(id)).thenReturn(List.of(
                donationWithUser(new BigDecimal("100.00"), user)
        ));

        List<DonorDisplayDto> top10 = donationService.getTopDonors(id, 10);

        assertThat(top10).hasSize(1);
    }

    // -------------------------------------------------------
    // deleteDonation
    // -------------------------------------------------------

    @Test
    void deleteDonation_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(donationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> donationService.deleteDonation(id))
                .isInstanceOf(com.sakute.project_fumo_backend.controller.exception.NotFoundException.class);
    }

    @Test
    void deleteDonation_shouldDeleteDonation_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        Donation d = donation(new BigDecimal("100.00"));
        when(donationRepository.findById(id)).thenReturn(Optional.of(d));

        donationService.deleteDonation(id);

        verify(donationRepository).delete(d);
    }

    // -------------------------------------------------------
    // helpers
    // -------------------------------------------------------

    private DonationRequest request(BigDecimal amount) {
        return request(amount, UUID.randomUUID());
    }

    private DonationRequest request(BigDecimal amount, UUID fundraisingId) {
        DonationRequest r = new DonationRequest();
        r.setAmount(amount);
        r.setFundraisingId(fundraisingId);
        r.setIsAnonymous(false);
        return r;
    }

    private Donation donation(BigDecimal amount) {
        Donation d = new Donation();
        d.setAmount(amount);
        d.setCreatedAt(Timestamp.from(Instant.now()));
        d.setIsAnonymous(false);
        return d;
    }

    private Donation donationWithUser(boolean anonymous) {
        Donation d = donation(new BigDecimal("100.00"));
        d.setDonor(user("Andrii Doe"));
        d.setIsAnonymous(anonymous);
        return d;
    }

    private Donation donationWithUser(BigDecimal amount, User user) {
        Donation d = donation(amount);
        d.setDonor(user);
        d.setIsAnonymous(false);
        return d;
    }

    private User user(String fullName) {
        User u = new User();
        u.setFullName(fullName);
        return u;
    }
}
