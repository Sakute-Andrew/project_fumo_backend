package com.sakute.project_fumo_backend.domain.enteties.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter // Замінили @Data на безпечні Getter/Setter
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @Column(name = "user_id")
    private UUID id; // Перейменували на id, щоб не плутатися

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // 🔑 Це каже Hibernate: використовуй ID юзера як ID для налаштувань
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "email_notifications", nullable = false)
    private boolean emailNotifications = true; // ✅ Правильне встановлення дефолту

    @Column(name = "marketing_opt_in", nullable = false)
    private boolean marketingOptIn = false;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "theme", length = 50)
    private String theme;

    @Column(name = "accessibility_options", columnDefinition = "text")
    private String accessibilityOptions;
}