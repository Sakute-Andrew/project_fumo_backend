package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    Optional<Comment> findByCommentId(Long postId);
    Optional<Comment> findByCommentIdAndUserId(Long postId, Long userId);
    Optional<Comment> findByUserId(Long userId);

}
