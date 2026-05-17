package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FundraisingRepository  extends JpaRepository<Fundraising, UUID> {
    Optional<Fundraising> findFundraisingById(UUID fundraisingId);
    Optional<Fundraising> findByOwner(User owner);
    // Або якщо хочеш шукати саме по ID юзера, а не передавати об'єкт:
    Optional<Fundraising> findByOwner_UserId(UUID userId);
    // (Заміни UserId на те, як називається поле ID в класі User)
    Optional<Fundraising> findByCategory_Id(Long categoryId);
    // (Примітка: заміни 'Id' на реальну назву поля ID в класі FundraisingCategory, наприклад findByCategory_CategoryId)
    Optional<Fundraising> findByTitle(String title);
    Optional<Fundraising> findByDescription(String description);

    Optional<Fundraising> findFundraisingByStartDate(Timestamp startDate);
    Optional<Fundraising> findByEndDate(Timestamp endDate);
    Optional<Fundraising> findByCreatedAt(Timestamp createdAt);

    List<Fundraising> findByOwnerUserIdAndStatus(UUID userId, Fundraising.Status status);



    // Пошук за категорією
    Page<Fundraising> findByCategory_Id(Long categoryId, Pageable pageable);

    Page<Fundraising> findByCategory_IdAndTitleContainingIgnoreCase(Long categoryId, String title, Pageable pageable);

    // Пошук за назвою або описом
    Page<Fundraising> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description, Pageable pageable);

    // Активні фандрейзинги (не закінчились)
    Page<Fundraising> findByEndDateAfter(Timestamp date, Pageable pageable);

    // Фандрейзинги що закінчуються скоро
    Page<Fundraising> findByEndDateBetweenOrderByEndDateAsc(
            Timestamp startDate, Timestamp endDate, Pageable pageable);

    // Сортування за поточною сумою (популярні) - використовуємо підзапит для сортування
    @Query("SELECT f FROM Fundraising f ORDER BY (SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.fundraising = f) DESC")
    Page<Fundraising> findAllByOrderByCurrentAmountDesc(Pageable pageable);

    // Фандрейзинги з високим відсотком досягнення мети - використовуємо підзапит
    @Query("SELECT f FROM Fundraising f WHERE ((SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.fundraising = f) / f.goalAmount) * 100 >= :percentage")
    Page<Fundraising> findByProgressPercentageGreaterThanEqual(@Param("percentage") double percentage, Pageable pageable);

    // Пошук за автором (якщо є поле author)
    // Page<Fundraising> findByAuthorId(UUID authorId, Pageable pageable);

    // Підрахунок активних фандрейзингів
    @Query("SELECT COUNT(f) FROM Fundraising f WHERE f.endDate > :currentDate")
    long countActiveFundraisings(@Param("currentDate") Timestamp currentDate);

    // Підрахунок фандрейзингів за категорією
    long countByCategory_Id(Long categoryId);

    // Нещодавно створені фандрейзинги
    @Query("SELECT f FROM Fundraising f WHERE f.createdAt >= :date ORDER BY f.createdAt DESC")
    Page<Fundraising> findRecentFundraisings(@Param("date") Timestamp date, Pageable pageable);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.fundraising.id = :id")
    BigDecimal sumDonationsByFundraisingId(@Param("id") UUID id);
}
