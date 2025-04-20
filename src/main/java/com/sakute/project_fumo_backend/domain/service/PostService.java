package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService extends Service<UserPost, UUID> {

    ResponseEntity<List<UserPost>> findByTitle(String title);
}
