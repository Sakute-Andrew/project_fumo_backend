package com.sakute.project_fumo_backend.domain.enteties.post;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "post_text", columnDefinition = "TEXT", length = 22555)
    private String postText;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "photo")
    private String photo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // не userId — це User об'єкт

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_topic_id")
    private PostTagTopic topic;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}