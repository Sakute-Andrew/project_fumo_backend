package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl extends ServiceGeneric<UserPost, UUID> implements PostService  {

    private final UserPostRepository userPostRepository;

    @Autowired
    public PostServiceImpl(UserPostRepository userPostRepository) {
        super(userPostRepository);
        this.userPostRepository = userPostRepository;
    }

    @Override
    public ResponseEntity<List<UserPost>> findByTitle(String title) {
        return Optional.of(userPostRepository.findUserPostByPostHeader(title))
                .filter(List::isEmpty)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
