package com.sakute.project_fumo_backend.domain.enteties;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

