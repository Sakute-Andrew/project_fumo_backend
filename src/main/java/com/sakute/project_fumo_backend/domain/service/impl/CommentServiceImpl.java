package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.dto.comment.CommentDto;
import com.sakute.project_fumo_backend.domain.dto.comment.CommentMapper;
import com.sakute.project_fumo_backend.domain.dto.comment.CommentResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.CommentService;
import com.sakute.project_fumo_backend.repository.jpa_repo.CommentRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceGeneric<Comment, Long> implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentDtoMapper;
    private final UserRepository userRepository;
    private final UserPostRepository userPostRepository;

    @Autowired
    protected CommentServiceImpl(CommentRepository commentRepository,
                                 CommentMapper commentDtoMapper,
                                 UserRepository userRepository, UserPostRepository userPostRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.commentDtoMapper = commentDtoMapper;
        this.userRepository = userRepository;
        this.userPostRepository = userPostRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByCommentId(UUID postId) {
        List<Comment> comments = commentRepository.findByPost_UserPostId(postId);
        return commentDtoMapper.toResponseDtoList(comments);
    }

    @Transactional
    public boolean createComment(CommentDto commentDto) {
        try {
            Comment comment = commentDtoMapper.toEntity(commentDto);

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            UserPost post = userPostRepository.findByUserPostId(commentDto.getUserPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + commentDto.getUserPostId()));

            comment.setPost(post);
            comment.setAuthor(user);

            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteByCommentId(UUID postId, Long commentId) {
        // Метод повертає кількість видалених рядків
        int deletedCount = commentRepository.deleteByPostIdAndCommentId(postId, commentId);

        // Повертаємо true, якщо видалено хоча б 1 рядок, і false, якщо ні
        return deletedCount > 0;
    }

    public boolean isCommentAuthor(Long commentId, String username) throws NotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return comment.getAuthor().getUsername().equals(username);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> findAllForAdmin(UUID postId, Pageable pageable) {
        Page<Comment> commentsPage;

        if (postId != null) {
            // Переконайтеся, що в CommentRepository є метод:
            // Page<Comment> findByPost_UserPostId(UUID postId, Pageable pageable);
            commentsPage = commentRepository.findByPost_UserPostId(postId, pageable);
        } else {
            commentsPage = commentRepository.findAll(pageable);
        }

        // Використовуємо ваш мапер або пишемо конвертацію вручну, якщо мапер не підтримує Page
        return commentsPage.map(commentDtoMapper::toResponseDto);
    }

    // Спрощене видалення по ID (для адмінки)
    @Transactional
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}