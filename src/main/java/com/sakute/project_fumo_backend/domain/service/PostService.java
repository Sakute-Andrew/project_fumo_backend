package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;

import java.util.Optional;
import java.util.UUID;

public interface PostService extends Service<UserPost, UUID> {

    Optional<UserPost> findByTitle(String title);
}
