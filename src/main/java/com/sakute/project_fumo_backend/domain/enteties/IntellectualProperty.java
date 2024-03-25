package com.sakute.project_fumo_backend.domain.enteties;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "intellectual_property")
public class IntellectualProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_id")
    private UUID ipId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type_ip", nullable = false)
    private String typeIp;

    @Column(name = "user_id", nullable = false)
    private UUID ownerId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "price", scale = 2)
    private BigDecimal price;

    @Column(name = "category_id", nullable = false)
    private Long category;

    @Column(name = "file_ip")
    private byte[] fileIp;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private IntellectualPropertyCategory intellectualPropertyCategory;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

