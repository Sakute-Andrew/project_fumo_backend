package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Top-level comments only (no parent) with their replies
    List<Comment> findByPost_UserPostIdAndParentCommentIsNull(UUID postId);

    // Кастомний запит для видалення (найшвидший варіант)
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.userPostId = :postId AND c.commentId = :commentId")
    int deleteByPostIdAndCommentId(@Param("postId") UUID postId, @Param("commentId") Long commentId);

    Page<Comment> findByPost_UserPostId(UUID postId, Pageable pageable);

}