package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "intellectual_property_comments")
public class IntellectualPropertyComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User userId;

    @Column(name = "ip_id", nullable = false)
    private UUID ipId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "ip_id", insertable = false, updatable = false)
    private IntellectualProperty intellectualProperty;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}
