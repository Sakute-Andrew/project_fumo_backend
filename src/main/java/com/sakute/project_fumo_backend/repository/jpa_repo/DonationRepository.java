package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    List<Donation> findByFundraising_Id(UUID fundraisingId);

    List<Donation> findByFundraising_IdOrderByCreatedAtDesc(UUID fundraisingId);

    Page<Donation> findByFundraising_Id(UUID fundraisingId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.fundraising.id = :fundraisingId")
    BigDecimal sumByFundraisingId(@Param("fundraisingId") UUID fundraisingId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d")
    BigDecimal sumAllDonations();

    @Modifying
    @Transactional
    @Query("UPDATE Donation d SET d.donor = null WHERE d.donor.userId = :userId")
    void nullifyDonorByUserId(@Param("userId") UUID userId);
}
