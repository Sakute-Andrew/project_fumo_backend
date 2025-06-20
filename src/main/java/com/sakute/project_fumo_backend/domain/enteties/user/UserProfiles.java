package com.sakute.project_fumo_backend.domain.enteties.user;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "user_profiles")
public class UserProfiles {

    @Id
    @Column(name = "user_profile_id")
    private Long userProfileId;

    @Column(name = "bio")
    private String bio;
    //TODO rewrite to table as tag
    @Column(name = "areas_of_expertise")
    private String areasOfExpertise;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "website")
    private String website;

    @Column(name = "location")
    private String location;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

}

