package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.LicensingAgreements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LicensingAgreementsRepository  extends JpaRepository<LicensingAgreements, UUID> {
    Optional<LicensingAgreements> findByAgreementId(UUID agreementId);

}
