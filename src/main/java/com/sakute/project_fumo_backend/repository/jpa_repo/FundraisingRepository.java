package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
