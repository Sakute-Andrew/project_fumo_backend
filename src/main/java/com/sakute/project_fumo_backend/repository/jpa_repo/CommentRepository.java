package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    Optional<Comment> findByCommentId(Long postId);
    Optional<Comment> findByCommentIdAndUserId(Long postId, User userId);
    Optional<Comment> findByUserId(User userId);
    Optional<Comment> findCommentByUserPostId(UUID userPostId);
}
