package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingMapper;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.impl.FundraisingServiceImpl;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingCategoryRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundraisingServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private FundraisingCategoryRepository fundraisingCategoryRepository;
    @Mock private FundraisingRepository fundraisingRepository;
    @Mock private FundraisingMapper fundraisingMapper;

    @InjectMocks
    private FundraisingServiceImpl fundraisingService;

    // -------------------------------------------------------
    // getAllFundraising — routing
    // -------------------------------------------------------

    @Test
    void getAllFundraising_withCategoryAndSearch_callsCombinedQuery() {
        Long categoryId = 1L;
        String search = "cats";
        Pageable pageable = Pageable.unpaged();

        when(fundraisingRepository.findByCategory_IdAndTitleContainingIgnoreCase(categoryId, search, pageable))
                .thenReturn(Page.empty());

        fundraisingService.getAllFundraising(pageable, categoryId, search);

        verify(fundraisingRepository).findByCategory_IdAndTitleContainingIgnoreCase(categoryId, search, pageable);
        verifyNoMoreInteractions(fundraisingRepository);
    }

    @Test
    void getAllFundraising_withCategoryOnly_callsCategoryFilter() {
        Long categoryId = 2L;
        Pageable pageable = Pageable.unpaged();

        when(fundraisingRepository.findByCategory_Id(categoryId, pageable)).thenReturn(Page.empty());

        fundraisingService.getAllFundraising(pageable, categoryId, null);

        verify(fundraisingRepository).findByCategory_Id(categoryId, pageable);
        verifyNoMoreInteractions(fundraisingRepository);
    }

    @Test
    void getAllFundraising_withSearchOnly_callsTextSearch() {
        String search = "health";
        Pageable pageable = Pageable.unpaged();

        when(fundraisingRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                search, search, pageable)).thenReturn(Page.empty());

        fundraisingService.getAllFundraising(pageable, null, search);

        verify(fundraisingRepository).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                search, search, pageable);
    }

    @Test
    void getAllFundraising_withNoFilter_callsFindAll() {
        Pageable pageable = Pageable.unpaged();

        when(fundraisingRepository.findAll(pageable)).thenReturn(Page.empty());

        fundraisingService.getAllFundraising(pageable, null, null);

        verify(fundraisingRepository).findAll(pageable);
    }

    @Test
    void getAllFundraising_withWhitespaceSearch_stillCallsTextSearch() {
        String search = "   ";
        Pageable pageable = Pageable.unpaged();

        when(fundraisingRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                search, search, pageable)).thenReturn(Page.empty());

        fundraisingService.getAllFundraising(pageable, null, search);

        verify(fundraisingRepository)
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
    }

    // -------------------------------------------------------
    // getFundraisingById
    // -------------------------------------------------------

    @Test
    void getFundraisingById_shouldReturnDto_whenExists() {
        UUID id = UUID.randomUUID();
        Fundraising entity = activeFundraising();
        FundraisingDto dto = new FundraisingDto();

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(entity));
        when(fundraisingMapper.toDto(entity)).thenReturn(dto);

        assertThat(fundraisingService.getFundraisingById(id)).isEqualTo(dto);
    }

    @Test
    void getFundraisingById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(fundraisingRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fundraisingService.getFundraisingById(id))
                .isInstanceOf(NotFoundException.class);
    }

    // -------------------------------------------------------
    // createFundraising
    // -------------------------------------------------------

    @Test
    void createFundraising_shouldThrow_whenEndDateIsInPast() {
        FundraisingDto dto = new FundraisingDto();
        dto.setEndDate(Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));

        assertThatThrownBy(() -> fundraisingService.createFundraising(dto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("минулому");
    }

    @Test
    void createFundraising_shouldSetActiveStatusAndOwner() {
        FundraisingDto dto = new FundraisingDto();
        dto.setEndDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));

        Fundraising entity = new Fundraising();
        User owner = new User();
        FundraisingDto savedDto = new FundraisingDto();

        when(fundraisingMapper.toEntity(dto)).thenReturn(entity);
        when(userRepository.findByUsername("creator")).thenReturn(Optional.of(owner));
        when(fundraisingRepository.save(entity)).thenReturn(entity);
        when(fundraisingMapper.toDto(entity)).thenReturn(savedDto);

        mockSecurityContext("creator");

        FundraisingDto result = fundraisingService.createFundraising(dto);

        assertThat(entity.getStatus()).isEqualTo(Fundraising.Status.ACTIVE);
        assertThat(entity.getOwner()).isEqualTo(owner);
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(result).isEqualTo(savedDto);
    }

    @Test
    void createFundraising_shouldThrow_whenOwnerNotFound() {
        FundraisingDto dto = new FundraisingDto();
        dto.setEndDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));

        when(fundraisingMapper.toEntity(dto)).thenReturn(new Fundraising());
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockSecurityContext("ghost");

        assertThatThrownBy(() -> fundraisingService.createFundraising(dto))
                .isInstanceOf(RuntimeException.class);
    }

    // -------------------------------------------------------
    // updateFundraising
    // -------------------------------------------------------

    @Test
    void updateFundraising_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(fundraisingRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fundraisingService.updateFundraising(id, new FundraisingDto()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateFundraising_shouldThrow_whenFundraisingExpired() {
        UUID id = UUID.randomUUID();
        Fundraising expired = expiredFundraising();

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(expired));

        assertThatThrownBy(() -> fundraisingService.updateFundraising(id, new FundraisingDto()))
                .isInstanceOf(OperationNotAllowedException.class)
                .hasMessageContaining("завершений");
    }

    @Test
    void updateFundraising_shouldThrow_whenGoalLessThanCurrentAmount() {
        UUID id = UUID.randomUUID();
        Fundraising fundraising = activeFundraising();
        Donation d = new Donation();
        d.setAmount(new BigDecimal("500.00"));
        fundraising.getDonations().add(d);

        FundraisingDto updateDto = new FundraisingDto();
        updateDto.setGoalAmount(new BigDecimal("100.00"));

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(fundraising));

        assertThatThrownBy(() -> fundraisingService.updateFundraising(id, updateDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("суму");
    }

    @Test
    void updateFundraising_shouldSucceed_whenValid() {
        UUID id = UUID.randomUUID();
        Fundraising fundraising = activeFundraising();
        FundraisingDto updateDto = new FundraisingDto();
        FundraisingDto expectedDto = new FundraisingDto();

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(fundraising));
        when(fundraisingRepository.save(fundraising)).thenReturn(fundraising);
        when(fundraisingMapper.toDto(fundraising)).thenReturn(expectedDto);

        FundraisingDto result = fundraisingService.updateFundraising(id, updateDto);

        assertThat(result).isEqualTo(expectedDto);
        verify(fundraisingMapper).updateEntityFromDto(updateDto, fundraising);
        verify(fundraisingRepository).save(fundraising);
    }

    // -------------------------------------------------------
    // deleteFundraising
    // -------------------------------------------------------

    @Test
    void deleteFundraising_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(fundraisingRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fundraisingService.deleteFundraising(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteFundraising_shouldThrow_whenHasDonations() {
        UUID id = UUID.randomUUID();
        Fundraising fundraising = activeFundraising();
        Donation d = new Donation();
        d.setAmount(new BigDecimal("1000.00"));
        fundraising.getDonations().add(d);

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(fundraising));

        assertThatThrownBy(() -> fundraisingService.deleteFundraising(id))
                .isInstanceOf(OperationNotAllowedException.class)
                .hasMessageContaining("донат");
    }

    @Test
    void deleteFundraising_shouldDelete_whenNoDonations() {
        UUID id = UUID.randomUUID();
        Fundraising fundraising = activeFundraising();

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(fundraising));

        fundraisingService.deleteFundraising(id);

        verify(fundraisingRepository).delete(fundraising);
    }

    // -------------------------------------------------------
    // isFundraisingOwner
    // -------------------------------------------------------

    @Test
    void isFundraisingOwner_shouldReturnTrue_whenEmailMatches() {
        UUID id = UUID.randomUUID();
        User owner = new User();
        owner.setEmail("owner@test.com");
        Fundraising fundraising = activeFundraising();
        fundraising.setOwner(owner);

        when(fundraisingRepository.findById(id)).thenReturn(Optional.of(fundraising));

        assertThat(fundraisingService.isFundraisingOwner(id, "owner@test.com")).isTrue();
    }

    @Test
    void isFundraisingOwner_shouldReturnFalse_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(fundraisingRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(fundraisingService.isFundraisingOwner(id, "anyone@test.com")).isFalse();
    }

    // -------------------------------------------------------
    // helpers
    // -------------------------------------------------------

    private Fundraising activeFundraising() {
        Fundraising f = new Fundraising();
        f.setEndDate(Timestamp.from(Instant.now().plus(30, ChronoUnit.DAYS)));
        f.setGoalAmount(new BigDecimal("10000.00"));
        return f;
    }

    private Fundraising expiredFundraising() {
        Fundraising f = new Fundraising();
        f.setEndDate(Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        f.setGoalAmount(new BigDecimal("10000.00"));
        return f;
    }

    private void mockSecurityContext(String username) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }
}
