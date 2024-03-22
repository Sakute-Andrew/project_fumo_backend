package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
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
    @GetMapping("/search/posts?q={query}")
    public List<UserPost> getPostByName(@RequestParam String query) {
        return null;
    }
    @PostMapping("/save-post")
    public UserPost savePost(UserPost post) {
        return null;
    }

    @GetMapping("/explore-posts")
    public List<UserPost> getAllPosts() {
        return postService.findAll();
    }
}
