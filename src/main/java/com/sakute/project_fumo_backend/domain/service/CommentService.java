package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.dto.comment.CommentDto;
import com.sakute.project_fumo_backend.domain.dto.comment.CommentResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CommentService extends Service<Comment, Long> {

    List<CommentResponseDto> findByCommentId(UUID postId);

    boolean createComment(CommentDto commentDto);

    boolean deleteByCommentId(UUID postId, Long commentId);

    boolean isCommentAuthor(Long commentId, String username) throws NotFoundException, javassist.NotFoundException;

    Page<CommentResponseDto> findAllForAdmin(UUID postId, Pageable pageable);

    void deleteById(Long commentId);
}
