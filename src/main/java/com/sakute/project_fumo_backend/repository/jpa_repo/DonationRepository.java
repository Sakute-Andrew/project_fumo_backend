package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    // Spring зрозуміє це як: зайти в поле fundraising і взяти його id
    List<Donation> findByFundraising_Id(UUID fundraisingId);

    // У Query ми звертаємось до поля-об'єкта d.fundraising
    @Query("SELECT d FROM Donation d WHERE d.fundraising.id = ?1 ORDER BY d.amount DESC")
    List<Donation> findTopDonorsByFundraisingId(UUID fundraisingId);

    List<Donation> findByFundraising_IdOrderByCreatedAtDesc(UUID fundraisingId);

    Page<Donation> findByFundraising_Id(UUID fundraisingId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.fundraising.id = :fundraisingId")
    BigDecimal sumDonationsByFundraisingId(@Param("fundraisingId") UUID fundraisingId);


    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.fundraising.id = :fundraisingId")
    BigDecimal sumByFundraisingId(@Param("fundraisingId") UUID fundraisingId);
}
