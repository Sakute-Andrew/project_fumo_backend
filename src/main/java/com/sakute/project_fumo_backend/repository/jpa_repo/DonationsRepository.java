package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Donations;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DonationsRepository extends RepositoryFactory<Donations, UUID> {


}
