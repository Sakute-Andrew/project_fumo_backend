package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualPropertyLikes;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface IntellectualPropertyLikesRepository extends RepositoryFactory<IntellectualPropertyLikes, Long> {
    Optional<IntellectualPropertyLikes> findByUserId(User userId);
    Optional<IntellectualPropertyLikes> findIntellectualPropertyLikesByIpId(UUID intellectualPropertyId);
    Optional<IntellectualPropertyLikes> findByLikeId(Long likeId);
}
