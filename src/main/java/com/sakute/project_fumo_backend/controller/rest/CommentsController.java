package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.dto.CommentDto;
import com.sakute.project_fumo_backend.domain.service.dto.mapper.PageableMapper;
import com.sakute.project_fumo_backend.domain.service.impl.CommentServiceImpl;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import org.mapstruct.factory.Mappers;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/posts")
public class CommentsController {

    private final UserPostRepository userPostRepository;
    CommentServiceImpl commentService;

    @Autowired
    public CommentsController(CommentServiceImpl commentService, UserPostRepository userPostRepository) {
        this.commentService = commentService;
        this.userPostRepository = userPostRepository;
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> findCommentsByPostId(@PathVariable UUID postId) {
        return null;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> findAllByPostId() {
        return null;
    }
}
