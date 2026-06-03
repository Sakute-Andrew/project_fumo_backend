package com.sakute.project_fumo_backend.domain.enteties;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payout_requests")
@ToString(exclude = {"fundraising"})
@EqualsAndHashCode(exclude = {"fundraising"})
public class PayoutRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payout_id")
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraising_id", nullable = false)
    private Fundraising fundraising;
}