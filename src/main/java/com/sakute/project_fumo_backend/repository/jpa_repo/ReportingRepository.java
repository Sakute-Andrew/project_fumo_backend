package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Reporting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportingRepository  extends JpaRepository<Reporting, Long> {
    Optional<Reporting> findByReportId(Long reportId);
}
