package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    Optional<UserPost> findUserPostByUserPostId(Long userPostId);
    Optional<UserPost> findUserPostByUserPostIdAndUser(Long userPostId, User userId);
    Optional<UserPost> findUserPostsByUser(User userId);
}
