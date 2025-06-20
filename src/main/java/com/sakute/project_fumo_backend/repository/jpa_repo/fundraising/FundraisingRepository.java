package com.sakute.project_fumo_backend.repository.jpa_repo.fundraising;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface FundraisingRepository  extends JpaRepository<Fundraising, UUID> {
    Optional<Fundraising> findFundraisingById(UUID fundraisingId);
    Optional<Fundraising> findByUserId(User userId);
    Optional<Fundraising> findFundraisingByCategory(Long categoryId);
    Optional<Fundraising> findByTitle(String title);
    Optional<Fundraising> findByDescription(String description);

    Optional<Fundraising> findFundraisingByStartDate(Timestamp startDate);
    Optional<Fundraising> findByEndDate(Timestamp endDate);
    Optional<Fundraising> findByCreatedAt(Timestamp createdAt);

    // Пошук за категорією
    Page<Fundraising> findByCategory(Long category, Pageable pageable);

    // Пошук за категорією та назвою
    Page<Fundraising> findByCategoryAndTitleContainingIgnoreCase(
            Long category, String title, Pageable pageable);

    // Пошук за назвою або описом
    Page<Fundraising> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description, Pageable pageable);

    // Активні фандрейзинги (не закінчились)
    Page<Fundraising> findByEndDateAfter(Timestamp date, Pageable pageable);

    // Фандрейзинги що закінчуються скоро
    Page<Fundraising> findByEndDateBetweenOrderByEndDateAsc(
            Timestamp startDate, Timestamp endDate, Pageable pageable);

    // Сортування за поточною сумою (популярні)
    Page<Fundraising> findAllByOrderByCurrentAmountDesc(Pageable pageable);

    // Пошук за автором (якщо є поле author)
    // Page<Fundraising> findByAuthorId(UUID authorId, Pageable pageable);

    // Підрахунок активних фандрейзингів
    @Query("SELECT COUNT(f) FROM Fundraising f WHERE f.endDate > :currentDate")
    long countActiveFundraisings(@Param("currentDate") Timestamp currentDate);

    // Підрахунок фандрейзингів за категорією
    long countByCategory(Long category);

    // Фандрейзинги з високим відсотком досягнення мети
    @Query("SELECT f FROM Fundraising f WHERE (f.currentAmount / f.goalAmount) * 100 >= :percentage")
    Page<Fundraising> findByProgressPercentageGreaterThanEqual(
            @Param("percentage") double percentage, Pageable pageable);

    // Нещодавно створені фандрейзинги
    @Query("SELECT f FROM Fundraising f WHERE f.createdAt >= :date ORDER BY f.createdAt DESC")
    Page<Fundraising> findRecentFundraisings(@Param("date") Timestamp date, Pageable pageable);
}
