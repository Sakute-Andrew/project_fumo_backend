package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.domain.service.impl.CommentServiceImpl;
import com.sakute.project_fumo_backend.domain.service.impl.PostServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostServiceImpl postService;

    private final CommentServiceImpl commentService;

    @Autowired
    public PostController(PostServiceImpl postService, CommentServiceImpl commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<?> getPostByName(@RequestParam String q) {
        return ResponseEntity.ok(postService.findByName(q));
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody UserPost post) {
        return ResponseEntity.ok(postService.save(post));
    }

    @GetMapping("/explore")
    public ResponseEntity<List<UserPost>> getAllPosts() {
        return ResponseEntity.ok(postService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }
}
