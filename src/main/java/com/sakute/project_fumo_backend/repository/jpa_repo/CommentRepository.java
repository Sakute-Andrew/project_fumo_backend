package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends RepositoryFactory<Comment, Long> {
    Optional<Comment> findByCommentId(Long postId);
    Optional<Comment> findByCommentIdAndUserId(Long postId, User userId);
    Optional<Comment> findByUserId(User userId);

}
