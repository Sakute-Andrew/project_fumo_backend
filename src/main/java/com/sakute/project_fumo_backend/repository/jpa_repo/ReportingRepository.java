package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Reporting;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface ReportingRepository  extends RepositoryFactory<Reporting, Long> {
    Optional<Reporting> findByReportId(UUID reportId);
}
