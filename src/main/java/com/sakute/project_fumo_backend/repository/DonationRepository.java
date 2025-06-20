package com.sakute.project_fumo_backend.repository;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    List<Donation> findByFundraisingId(UUID fundraisingId);

    @Query("SELECT d FROM Donation d WHERE d.fundraisingId = ?1 ORDER BY d.amount DESC")
    List<Donation> findTopByDonorFundraisingId(UUID fundraisingId);
}
