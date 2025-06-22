package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.dto.CommentDto;
import com.sakute.project_fumo_backend.domain.service.impl.CommentServiceImpl;
import com.sakute.project_fumo_backend.repository.jpa_repo.post.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/posts")
public class CommentsController {

    private final UserPostRepository userPostRepository;
    private CommentServiceImpl commentService;

    @Autowired
    public CommentsController(CommentServiceImpl commentService, UserPostRepository userPostRepository) {
        this.commentService = commentService;
        this.userPostRepository = userPostRepository;
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> findCommentsByPostId(@PathVariable UUID postId) {
        return ResponseEntity.of(Optional.ofNullable(commentService.findByCommentId(postId)));
    }

    @PostMapping("/comment/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDto comment) {
            if(!commentService.createComment(comment)){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.ok().build();

    }

    @GetMapping("/comments/list")
    public ResponseEntity<?> findAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @DeleteMapping("/{postId}/comments/{id}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(#id, authentication.name)")
    public ResponseEntity<?> deleteComment(@PathVariable UUID postId,@PathVariable Long id) {
        return commentService.deleteByCommentId(postId, id);
    }

}
