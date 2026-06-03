package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.dto.post.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.service.CommentService;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.domain.service.impl.CommentServiceImpl;
import com.sakute.project_fumo_backend.domain.service.impl.PostServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Отримання всіх постів з підтримкою пагінації та пошуку
    @GetMapping
    public ResponseEntity<Page<UserPostDto>> getPosts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long topicId,
            Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(name, topicId, pageable));
    }

    // Отримання конкретного поста за ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable UUID id) throws NotFoundException {
        var post = postService.findPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserPostDto> createPost(@Valid @RequestBody UserPostDto post) {
        UserPostDto savedPost = postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePost(@PathVariable UUID id, @Valid @RequestBody UserPostDto post) {
        var updatedPost = postService.update(id, post);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/topics")
    public ResponseEntity<List<PostTagTopic>> getTopics() {
        return ResponseEntity.ok(postService.getPostTagTopic());
    }
}