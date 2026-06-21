package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.PayoutRequest;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PayoutRequestRepository extends JpaRepository<PayoutRequest, UUID> {
    long countByStatus(RequestStatus status);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PayoutRequest p WHERE p.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") RequestStatus status);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PayoutRequest p WHERE p.fundraising.id = :fundraisingId AND p.status IN :statuses")
    BigDecimal sumByFundraisingIdAndStatusIn(@Param("fundraisingId") UUID fundraisingId, @Param("statuses") List<RequestStatus> statuses);
}
