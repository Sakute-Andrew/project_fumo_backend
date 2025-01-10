package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.UserPostLike;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface UserPostLikeRepository  extends RepositoryFactory<UserPostLike, Long> {
    Optional<UserPostLike> findUserPostLikeByUserId(UUID userId);
    Optional<UserPostLike> findUserPostLikeByUserPost(UserPost userPostId);

}
