package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.dto.CommentDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.mapper.CommentMapper;
import com.sakute.project_fumo_backend.domain.enteties.dto.response.CommentResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.CommentService;
import com.sakute.project_fumo_backend.repository.jpa_repo.CommentRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceGeneric<Comment, Long> implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentDtoMapper;
    private final UserRepository userRepository;

    @Autowired
    protected CommentServiceImpl(CommentRepository commentRepository,
                                 CommentMapper commentDtoMapper,
                                 UserRepository userRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.commentDtoMapper = commentDtoMapper;
        this.userRepository = userRepository;
    }

    public List<CommentResponseDto> findByCommentId(UUID postId) {
        List<Comment> comments = commentRepository.findByUserPostId(postId);
        return commentDtoMapper.mapToResponseDtoList(comments);
    }

    public boolean createComment(CommentDto commentDto) {
        try {
            // Конвертуємо DTO в entity
            Comment comment = commentDtoMapper.mapToEntity(commentDto);

            // Завантажуємо користувача з бази даних
            User user = userRepository.findUserByUserId(commentDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + commentDto.getUserId()));

            comment.setUserId(user);

            // Зберігаємо коментар
            Comment savedComment = commentRepository.save(comment);
            log.info("Saved comment with user: " + savedComment.getUserId()); // Для дебагу

            return true;
        } catch (Exception e) {
            System.err.println("Error creating comment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<?> deleteByCommentId(UUID postId, Long commentId) {
        boolean deleted = commentRepository.deleteByUserPostIdAndCommentId(postId, commentId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public boolean isCommentAuthor(Long commentId, String username) throws NotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return comment.getUserId().getUsername().equals(username);
    }
}