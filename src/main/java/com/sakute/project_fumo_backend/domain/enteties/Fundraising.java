package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "fundraising")
public class Fundraising {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fundraising_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "goal_amount", nullable = false)
    private BigDecimal goalAmount;

    @Column(name = "current_amount", nullable = false)
    private BigDecimal currentAmount;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User userId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "category_id", nullable = false)
    private Long category;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private FundraisingCategory fundraisingCategory;


}

