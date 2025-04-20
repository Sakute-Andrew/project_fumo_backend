package com.sakute.project_fumo_backend.domain.enteties;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "ip_id", nullable = false)
    private UUID ipId;

    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;

    @Column(name = "total_amount", nullable = false, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "ip_id", insertable = false, updatable = false)
    private IntellectualProperty intellectualProperty;

}
