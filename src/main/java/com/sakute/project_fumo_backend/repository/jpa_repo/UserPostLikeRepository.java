package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    Optional<UserPostLike> findUserPostLikeByUserId(UUID userId);
    Optional<UserPostLike> findUserPostLikeByUserPost(UserPost userPostId);

}
