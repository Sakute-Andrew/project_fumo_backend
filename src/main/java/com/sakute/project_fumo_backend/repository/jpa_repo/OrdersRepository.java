package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Orders;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrdersRepository  extends JpaRepository<Orders, UUID> {
    Optional<Orders> findByOrderId(UUID orderId);
    Optional<Orders> findByCustomer(User userId);
    Optional<Orders> findByIpId(UUID fundraisingId);
}
