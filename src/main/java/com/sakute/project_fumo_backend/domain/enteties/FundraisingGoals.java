package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "fundraising_goals")
public class FundraisingGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraising_id", referencedColumnName = "fundraising_id", nullable = false)
    private Fundraising fundraising;

    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "achieved", nullable = false)
    private boolean achieved;

    public FundraisingGoals() {

    }
}
