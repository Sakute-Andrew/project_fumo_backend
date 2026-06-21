package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.service.FundraisingService;
import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingCategoryDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingListDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingMapper;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingCategoryRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.PayoutRequestRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Замінює твій ручний конструктор
public class FundraisingServiceImpl implements FundraisingService {

    private final UserRepository userRepository;
    private final FundraisingCategoryRepository fundraisingCategoryRepository;
    private final FundraisingRepository fundraisingRepository;
    private final FundraisingMapper fundraisingMapper;
    private final PayoutRequestRepository payoutRequestRepository;

    // --- READ METODS ---
    @Transactional(readOnly = true)
    public Page<FundraisingListDto> getAllFundraising(Pageable pageable, Long category, String search) {
        Specification<Fundraising> spec = Specification
                .where(byCategory(category))
                .and(bySearch(search));
        return fundraisingRepository.findAll(spec, pageable).map(fundraisingMapper::toListDto);
    }

    private Specification<Fundraising> byCategory(Long categoryId) {
        return categoryId != null
                ? (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId)
                : null;
    }

    private Specification<Fundraising> bySearch(String search) {
        if (search == null || search.isBlank()) return null;
        String pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
        );
    }

    public List<FundraisingCategoryDto> getCategories() {
        return fundraisingCategoryRepository.findAll()
                .stream()
                .map(c -> new FundraisingCategoryDto(c.getId(), c.getCategoryName()))
                .toList();
    }

    public Page<FundraisingListDto> getPopularFundraisings(Pageable pageable) {
        return fundraisingRepository.findAllByOrderByCurrentAmountDesc(pageable)
                .map(fundraisingMapper::toListDto); // Використовуємо наш новий мапер
    }

    @Transactional(readOnly = true)
    public FundraisingDto getFundraisingById(UUID id) {
        Fundraising fundraising = findByIdOrThrow(id);
        FundraisingDto dto = fundraisingMapper.toDto(fundraising);

        BigDecimal withdrawn = payoutRequestRepository.sumByFundraisingIdAndStatusIn(
            id, List.of(RequestStatus.PENDING, RequestStatus.APPROVED)
        );
        dto.setWithdrawnAmount(withdrawn != null ? withdrawn : BigDecimal.ZERO);

        return dto;
    }

    public Page<FundraisingListDto> getActiveFundraising(Pageable pageable) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return fundraisingRepository.findByEndDateAfter(now, pageable)
                .map(fundraisingMapper::toListDto); // ВИПРАВЛЕНО
    }

    public Page<FundraisingListDto> getEndingSoonFundraising(Pageable pageable) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp weekFromNow = new Timestamp(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L);

        return fundraisingRepository.findByEndDateBetweenOrderByEndDateAsc(now, weekFromNow, pageable)
                .map(fundraisingMapper::toListDto); // ВИПРАВЛЕНО
    }

    // --- WRITE METODS ---

    @Transactional
    public FundraisingDto createFundraising(FundraisingDto createDto) {
        // Уся валідація пустих полів робиться через @Valid у контролері!
        validateEndDate(createDto.getEndDate());

        Fundraising fundraising = fundraisingMapper.toEntity(createDto);
        fundraising.setId(UUID.randomUUID()); // Або дозволь базі самій згенерувати
        fundraising.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        fundraising.setStartDate(new Timestamp(System.currentTimeMillis()));
        fundraising.setStatus(Fundraising.Status.ACTIVE);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено"));
        fundraising.setOwner(owner);

        return fundraisingMapper.toDto(fundraisingRepository.save(fundraising));
    }

    // Цей метод викликатиметься автоматично з анотації @PreAuthorize
    public boolean isFundraisingOwner(UUID fundraisingId, String userEmail) {
        return fundraisingRepository.findById(fundraisingId)
                .map(fundraising -> fundraising.getOwner().getEmail().equals(userEmail))
                .orElse(false);
    }

    @Transactional
    public FundraisingDto updateFundraising(UUID id, FundraisingDto updateDto) {
        Fundraising fundraising = findByIdOrThrow(id);

        checkEditPermissions(fundraising);

        if (updateDto.getGoalAmount() != null && updateDto.getGoalAmount().compareTo(fundraising.getCurrentAmount()) < 0) {
            throw new InvalidInputException("Цільова сума не може бути меншою за вже зібрану суму");
        }
        if (updateDto.getEndDate() != null) {
            validateEndDate(updateDto.getEndDate());
        }

        // ✨ МАГІЯ: MapStruct сам замінить тільки ті поля, які не null
        fundraisingMapper.updateEntityFromDto(updateDto, fundraising);

        return fundraisingMapper.toDto(fundraisingRepository.save(fundraising));
    }

    @Transactional
    public void deleteFundraising(UUID id) {
        Fundraising fundraising = findByIdOrThrow(id);
        // checkDeletePermissions(fundraising);

        if (fundraising.getCurrentAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new OperationNotAllowedException("Неможливо видалити фандрейзинг з донатами.");
        }

        fundraisingRepository.delete(fundraising);
    }

    // --- ПРИВАТНІ ДОПОМІЖНІ МЕТОДИ (Щоб не дублювати код) ---

    private Fundraising findByIdOrThrow(UUID id) {
        return fundraisingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Фандрейзинг не знайдено"));
    }

    private void checkEditPermissions(Fundraising fundraising) {
        if (fundraising.getEndDate().before(new Timestamp(System.currentTimeMillis()))) {
            throw new OperationNotAllowedException("Неможливо редагувати завершений фандрейзинг");
        }
    }

    private void validateEndDate(Timestamp endDate) {
        if (endDate != null && endDate.before(new Timestamp(System.currentTimeMillis()))) {
            throw new InvalidInputException("Дата закінчення не може бути в минулому");
        }
    }
}