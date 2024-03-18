package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "user_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_post_id")
    private Long userPostId;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "post_header", nullable = false, length = 300)
    private String postHeader;

    @Column(name = "post_description")
    private String postDescription;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;

    @Column(name = "post_topic")
    private Long postTopic;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_topic", insertable = false, updatable = false)
    private PostTagTopic postTagTopic;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

