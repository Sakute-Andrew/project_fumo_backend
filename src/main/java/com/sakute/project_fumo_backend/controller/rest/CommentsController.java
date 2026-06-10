package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.comment.CommentDto;
import com.sakute.project_fumo_backend.domain.dto.comment.CommentResponseDto;
import com.sakute.project_fumo_backend.domain.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CommentResponseDto>> findAllComments(
            @RequestParam(required = false) UUID postId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.findAllForAdmin(postId, pageable));
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(#id, authentication.name)")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        // Тепер достатньо тільки ID коментаря для видалення
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- ПУБЛІЧНА ЧАСТИНА ---

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> findCommentsByPostId(@PathVariable UUID postId) {
        return ResponseEntity.ok(commentService.findByCommentId(postId));
    }

    @PostMapping("/comment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> createComment(@RequestBody @Validated CommentDto comment) {
        commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}


