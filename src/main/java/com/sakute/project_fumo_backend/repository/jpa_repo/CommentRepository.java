package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByUserId(User userId);
    List<Comment> findByUserPostId(UUID postId);
    boolean deleteByUserPostIdAndCommentId(UUID userPostId, Long commentId);
}
