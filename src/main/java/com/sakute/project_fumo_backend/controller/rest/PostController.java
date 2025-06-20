package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.PaginatedResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.service.impl.CommentServiceImpl;
import com.sakute.project_fumo_backend.domain.service.post.impl.PostServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;

    @Autowired
    public PostController(PostServiceImpl postService, CommentServiceImpl commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    // Отримання всіх постів з підтримкою пагінації та пошуку
    @GetMapping
    public ResponseEntity<PaginatedResponseDto<UserPostDto>> getPosts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit) throws NotFoundExeption {

        PaginatedResponseDto<UserPostDto> result;

        if (name != null && !name.trim().isEmpty()) {
            // Пошук за назвою з пагінацією
            result = postService.findByTitle(name, page, limit);
        } else {
            // Повернення всіх постів з пагінацією
            result = postService.findAllPaginated(page, limit);
        }

        return ResponseEntity.ok(result);
    }

    // Отримання конкретного поста за ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable UUID id) throws NotFoundExeption {
        var post = postService.findPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(@Valid @RequestBody UserPost post) {
        var savedPost = postService.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePost(@PathVariable UUID id, @Valid @RequestBody UserPostDto post) {
        var updatedPost = postService.update(post);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        var posts = postService.getPostTagTopic();
        return ResponseEntity.of(Optional.ofNullable(posts));
    }
}