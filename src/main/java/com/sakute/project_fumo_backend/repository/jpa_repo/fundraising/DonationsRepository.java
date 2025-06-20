package com.sakute.project_fumo_backend.repository.jpa_repo.fundraising;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DonationsRepository extends JpaRepository<Donation, UUID>{


}
