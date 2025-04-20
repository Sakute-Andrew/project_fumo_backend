package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.service.CommentService;
import com.sakute.project_fumo_backend.repository.jpa_repo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl extends ServiceGeneric<Comment, Long> implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    protected CommentServiceImpl(CommentRepository commentRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByCommentId(UUID postId) {
        return commentRepository.findCommentByUserPostId(postId).stream().toList();
    }

}
