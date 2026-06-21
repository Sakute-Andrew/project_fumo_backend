package com.sakute.project_fumo_backend.domain.enteties.intprop;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter // Замінили @Data
@Table(name = "intellectual_property")
public class IntellectualProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Для UUID краще використовувати @GeneratedValue(strategy = GenerationType.UUID) у нових версіях Hibernate
    @Column(name = "ip_id")
    private UUID ipId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 5238)
    private String description;

    @Column(name = "type_ip", nullable = false)
    private String typeIp;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "file_ip")
    private String fileIp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IpStatus status;

    // ✅ Прибрали insertable=false, updatable=false
    // ✅ Додали fetch = FetchType.LAZY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private IntellectualPropertyCategory intellectualPropertyCategory;
}