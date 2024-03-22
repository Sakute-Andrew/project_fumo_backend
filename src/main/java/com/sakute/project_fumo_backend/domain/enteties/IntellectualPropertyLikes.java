package com.sakute.project_fumo_backend.domain.enteties;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "intellectual_property_likes")
public class IntellectualPropertyLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User userId;

    @Column(name = "ip_id", nullable = false)
    private Long ipId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "ip_id", insertable = false, updatable = false)
    private IntellectualProperty intellectualProperty;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

