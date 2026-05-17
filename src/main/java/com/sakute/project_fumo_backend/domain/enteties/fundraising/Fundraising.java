package com.sakute.project_fumo_backend.domain.enteties.fundraising;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fundraising")
public class Fundraising {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fundraising_id")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "goal_amount", nullable = false)
    private BigDecimal goalAmount;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;  // тепер зберігається в БД

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;  // не userId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private FundraisingCategory category;  // не два маппінги

    @OneToMany(mappedBy = "fundraising", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations = new ArrayList<>();

    public enum Status {
        ACTIVE, COMPLETED, ARCHIVED
    }

    // Додаємо обчислюваний метод
    public BigDecimal getCurrentAmount() {
        if (donations == null || donations.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return donations.stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

