package com.sakute.project_fumo_backend.domain.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfiles {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "bio")
    private String bio;

    @Column(name = "areas_of_expertise")
    private String areasOfExpertise;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Column(name = "website")
    private String website;

    @Column(name = "location")
    private String location;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

