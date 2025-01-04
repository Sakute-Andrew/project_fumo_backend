package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_t")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @JoinColumn(name = "user_role", insertable = false, updatable = false)
    private Long userRole;

    @Column(name = "last_login_datetime", nullable = false)
    private Timestamp lastLoginDatetime;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

}

