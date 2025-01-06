package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts?q={query}")
    public ResponseEntity<?> getPostByName(@RequestParam String query) {
        return null;
    }

    @PostMapping("/save-post")
    public ResponseEntity<?> savePost(UserPost post) {
        return null;
    }

    @GetMapping("/explore-posts")
    public ResponseEntity<List<?>> getAllPosts() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@RequestBody UserPost post) {
        return ResponseEntity.ok(postService.save(post));}

    @PostMapping("/delete-post")
    public void deletePost(@RequestParam UserPost post) {
        postService.delete(post);
    }
    @GetMapping("/post")
    public ResponseEntity<?> getCommentsbyPostId(@RequestParam Long postId) {
        return null;
    }
}
