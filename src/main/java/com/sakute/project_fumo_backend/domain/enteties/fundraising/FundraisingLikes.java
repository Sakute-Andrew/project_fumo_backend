package com.sakute.project_fumo_backend.domain.enteties.fundraising;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Entity
@Table(name = "fundraising_likes", schema = "public")
public class FundraisingLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "fundraising_id", nullable = false)
    private Fundraising fundraising;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public FundraisingLikes() {

    }
}

