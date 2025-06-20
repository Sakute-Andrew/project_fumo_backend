package com.sakute.project_fumo_backend.domain.enteties.dto.mapper;

import com.sakute.project_fumo_backend.domain.enteties.dto.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PostMapper {

    public UserPostDto toDto(@NotNull UserPost post) {
        UserDto userDto = new UserDto(post.getUserId().getUserId(), post.getUserId().getUsername());
        return new UserPostDto(
                post.getUserPostId(),
                post.getPostHeader(),
                post.getPostDescription(),
                post.getPostText(),
                post.getCreatedAt(),
                post.getPostTagTopic(),
                post.getPhoto(),
                userDto

        );
    }

    public List<UserPostDto> toDtoList(List<UserPost> posts) {
        return posts.stream().map(this::toDto).toList();
    }

    public Page<UserPostDto> toDtoPage(Page<UserPost> postPage) {
        List<UserPostDto> content = toDtoList(postPage.getContent());
        return new PageImpl<>(content, postPage.getPageable(), postPage.getTotalElements());
    }

    public void updateEntityFromDto(UserPostDto dto, UserPost entity) {
        entity.setPostHeader(dto.getPostHeader());
        entity.setPostDescription(dto.getPostBody());
        entity.setPostText(dto.getPostText());
        entity.setPostTagTopic(dto.getPostTopic());
        entity.setPhoto(dto.getPhoto());
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        // createdAt не змінюємо
    }


}