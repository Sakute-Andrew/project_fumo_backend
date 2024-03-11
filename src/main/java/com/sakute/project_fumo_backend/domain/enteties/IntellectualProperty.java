package com.sakute.project_fumo_backend.domain.enteties;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "intellectual_property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntellectualProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_id")
    private Long ipId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type_ip", nullable = false)
    private String typeIp;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "price", scale = 2)
    private BigDecimal price;

    @Column(name = "category", nullable = false)
    private Long category;

    @Column(name = "file_ip")
    private byte[] fileIp;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "category", insertable = false, updatable = false)
    private IntellectualPropertyCategory intellectualPropertyCategory;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

