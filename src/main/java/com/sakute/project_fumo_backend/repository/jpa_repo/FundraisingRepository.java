package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface FundraisingRepository  extends RepositoryFactory<Fundraising, UUID> {
    Optional<Fundraising> findFundraisingById(UUID fundraisingId);
    Optional<Fundraising> findByUserId(User userId);
    Optional<Fundraising> findFundraisingByCategory(Long categoryId);
    Optional<Fundraising> findByTitle(String title);
    Optional<Fundraising> findByDescription(String description);

    Optional<Fundraising> findFundraisingByStartDate(Timestamp startDate);
    Optional<Fundraising> findByEndDate(Timestamp endDate);
    Optional<Fundraising> findByCreatedAt(Timestamp createdAt);
}
