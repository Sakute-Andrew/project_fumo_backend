package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User userId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "user_post_id", nullable = false)
    private UUID userPostId;

    public Comment() {

    }
}

