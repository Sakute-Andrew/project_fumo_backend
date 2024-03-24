package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

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
}
