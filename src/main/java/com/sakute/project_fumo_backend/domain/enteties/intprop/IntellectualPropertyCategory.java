package com.sakute.project_fumo_backend.domain.enteties.intprop;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "intellectual_property_category")
public class IntellectualPropertyCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}
