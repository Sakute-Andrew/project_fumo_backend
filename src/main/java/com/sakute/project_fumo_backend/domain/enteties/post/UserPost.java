package com.sakute.project_fumo_backend.domain.enteties.post;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "user_id")
    private User userId;


    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "post_header", nullable = false, length = 300)
    private String postHeader;

    @Column(name = "post_description")
    private String postDescription;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "post_topic")
    private Long postTopic;
    @ManyToOne
    @JoinColumn(name = "post_topic", insertable = false, updatable = false)
    private PostTagTopic postTagTopic;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

