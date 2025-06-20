package com.sakute.project_fumo_backend.domain.service.post;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.dto.PaginatedResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PostService  {

    UserPostDto findByTitle(String title);
    // В PostServiceImpl потрібно додати ці методи:
     PaginatedResponseDto<UserPostDto> findByTitle(String title, int page, int limit);
     Page<UserPostDto> findAllPaginated(String title, int page, int limit);
     Page<UserPostDto> searchByTitle(String title, Pageable pageable);
     Page<UserPostDto> findByCategory(String category, Pageable pageable);
     Page<UserPostDto> findByUserId(UUID userId, Pageable pageable);
     List<UserPostDto> findAllForExplore(int limit);
     List<PostTagTopic> getPostTagTopic();
     UserPost update(UserPostDto post);
     boolean isOwner(UUID postId, String username);
}
