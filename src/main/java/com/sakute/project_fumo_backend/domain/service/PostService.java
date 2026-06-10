package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.post.PostTopicDto;
import com.sakute.project_fumo_backend.domain.dto.post.UserPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PostService {

    UserPostDto findPostById(UUID id);
    UserPostDto savePost(UserPostDto userPostDto);
    void deleteById(UUID id);
    Page<UserPostDto> findAll(String name, Long topicId, Pageable pageable);
    List<PostTopicDto> getPostTagTopic();
    UserPostDto update(UUID id, UserPostDto post);
}
