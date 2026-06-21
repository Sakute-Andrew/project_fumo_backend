package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FundraisingRepository extends JpaRepository<Fundraising, UUID>, JpaSpecificationExecutor<Fundraising> {

    List<Fundraising> findByOwnerUserIdAndStatus(UUID userId, Fundraising.Status status);

    long countByStatus(Fundraising.Status status);

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

}
