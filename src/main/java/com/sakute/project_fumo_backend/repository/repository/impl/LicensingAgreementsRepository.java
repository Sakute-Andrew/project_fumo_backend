package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.LicensingAgreements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LicensingAgreementsRepository  extends JpaRepository<LicensingAgreements, Long> {
    Optional<LicensingAgreements> findByAgreementId(Long agreementId);

}
