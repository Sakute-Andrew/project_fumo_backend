package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.post.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PostService  {

    UserPostDto findByTitle(String title);
    // В PostServiceImpl потрібно додати ці методи
     Page<UserPostDto> searchByTitle(String title, Pageable pageable);
     Page<UserPostDto> findByCategory(String category, Pageable pageable);
     Page<UserPostDto> findByUserId(UUID userId, Pageable pageable);
     List<UserPostDto> findAllForExplore(int limit);
     Page<UserPostDto> findAll(String name, Pageable pageable);
     List<PostTagTopic> getPostTagTopic();
     UserPostDto update(UUID id, UserPostDto post);
     boolean isOwner(UUID postId, String username);
}
