package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fundraising_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundraisingUpdates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_id")
    private Long updateId;

    @Column(name = "fundraising_id", nullable = false)
    private Long fundraisingId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "fundraising_id", insertable = false, updatable = false)
    private Fundraising fundraising;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

