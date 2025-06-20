package com.sakute.project_fumo_backend.domain.enteties.post;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_post")
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_post_id")
    private UUID userPostId;

    @Column(name = "post_header", nullable = false, length = 300)
    private String postHeader;

    @Column(name = "post_description")
    private String postDescription;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "post_topic", insertable = false, updatable = false)
    private PostTagTopic postTagTopic;

    @Column(name = "photo")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "user_id")
    private User userId;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

