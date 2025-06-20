package com.sakute.project_fumo_backend.domain.enteties.fundraising;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "donation_id")
    private UUID id;

    @Column(name = "fundraising_id", nullable = false)
    private UUID fundraisingId;

    @ManyToOne
    @JoinColumn(name = "fundraising_id", insertable = false, updatable = false)
    private Fundraising fundraising;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "donor_name")
    private String donorName;

    @Column(name = "donor_email")
    private String donorEmail;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;
}
