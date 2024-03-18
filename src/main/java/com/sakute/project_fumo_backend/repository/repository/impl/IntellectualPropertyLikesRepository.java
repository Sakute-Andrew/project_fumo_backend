package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualPropertyLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntellectualPropertyLikesRepository extends JpaRepository<IntellectualPropertyLikes, Long> {
    Optional<IntellectualPropertyLikes> findByUserId(Long userId);
    Optional<IntellectualPropertyLikes> findIntellectualPropertyLikesByIpId(Long intellectualPropertyId);
    Optional<IntellectualPropertyLikes> findByLikeId(Long likeId);
}
