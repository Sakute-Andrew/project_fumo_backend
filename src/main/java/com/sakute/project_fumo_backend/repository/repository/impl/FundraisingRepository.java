package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface FundraisingRepository  extends JpaRepository<Fundraising, Long> {
    Optional<Fundraising> findFundraisingById(Long fundraisingId);
    Optional<Fundraising> findByUserId(User userId);
    Optional<Fundraising> findFundraisingByCategory(Long categoryId);
    Optional<Fundraising> findByTitle(String title);
    Optional<Fundraising> findByDescription(String description);

    Optional<Fundraising> findFundraisingByStartDate(Timestamp startDate);
    Optional<Fundraising> findByEndDate(Timestamp endDate);
    Optional<Fundraising> findByCreatedAt(Timestamp createdAt);
}
