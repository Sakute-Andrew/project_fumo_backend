package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.Orders;
import com.sakute.project_fumo_backend.domain.enteties.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository  extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderId(Long orderId);
    Optional<Orders> findByCustomer(User userId);
    Optional<Orders> findByIpId(Long fundraisingId);
}
