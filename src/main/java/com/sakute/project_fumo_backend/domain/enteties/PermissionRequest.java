package com.sakute.project_fumo_backend.domain.enteties;

import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission_requests")
public class PermissionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_permission", nullable = false)
    private Permission requestedPermission;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED


    @Column(columnDefinition = "TEXT")
    private String message; // Чому юзер хоче це право (опціонально)

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}