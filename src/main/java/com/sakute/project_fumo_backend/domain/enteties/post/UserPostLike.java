package com.sakute.project_fumo_backend.domain.enteties.post;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.Data;


import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "user_post_like")
@Data
public class UserPostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_post_id", insertable = false, updatable = false)
    private UserPost userPost;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

