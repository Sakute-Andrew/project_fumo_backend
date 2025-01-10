package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.LicensingAgreements;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface LicensingAgreementsRepository  extends RepositoryFactory<LicensingAgreements, UUID> {
    Optional<LicensingAgreements> findByAgreementId(UUID agreementId);

}
