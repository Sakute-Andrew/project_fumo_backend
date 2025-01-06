package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "donations")
public class Donations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "donation_id")
    private Long id;

    @Column(name = "fundraising_id", nullable = false)
    private UUID fundraisingId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "donation_date", nullable = false)
    private Timestamp donationDate;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "anonymous", nullable = false)
    private boolean anonymous;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User donor;

    @ManyToOne
    @JoinColumn(name = "fundraising_id", insertable = false, updatable = false)
    private Fundraising fundraising;

    public Donations() {

    }
}

