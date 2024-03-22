package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email_notifications", nullable = false, columnDefinition = "boolean default true")
    private boolean emailNotifications;

    @Column(name = "marketing_opt_in", nullable = false, columnDefinition = "boolean default false")
    private boolean marketingOptIn;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "theme", length = 50)
    private String theme;

    @Column(name = "accessibility_options", columnDefinition = "text")
    private String accessibilityOptions;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

