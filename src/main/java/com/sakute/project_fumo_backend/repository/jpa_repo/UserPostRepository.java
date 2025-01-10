package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPostRepository extends JpaRepository<UserPost, UUID> {
    Optional<UserPost> findUserPostByUserPostId(UUID userPostId);
    Optional<UserPost> findUserPostByUserPostIdAndUserId(UUID userPostId, User userId);
    Optional<UserPost> findUserPostsByUserId(User userId);
    @Query(value = "SELECT * FROM user_post", nativeQuery = true)
    List<UserPost> findAllBy();
    Optional<UserPost> findUserPostByPostHeader(String name);

}
