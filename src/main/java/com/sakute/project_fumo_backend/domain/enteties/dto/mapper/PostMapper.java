package com.sakute.project_fumo_backend.domain.enteties.dto.mapper;

import com.sakute.project_fumo_backend.domain.enteties.dto.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
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

    public UserPost toEntity(UserPostDto dto) {
        UserPost post = new UserPost();

        post.setUserPostId(dto.getId());
        post.setPostHeader(dto.getPostHeader());
        post.setPostDescription(dto.getPostBody());
        post.setPostText(dto.getPostText());
        post.setCreatedAt(dto.getPostDate() != null
                ? new Timestamp(dto.getPostDate().getTime())
                : new Timestamp(System.currentTimeMillis()));

        post.setPostTagTopic(dto.getPostTopic()); // це об'єкт, не забудь перевірити null

        post.setPhoto(dto.getPhoto());

        // !!! Тут userId потрібно окремо з userDto — або через сервіс, або якось ще
        if (dto.getUser() != null) {
            // ⚠️ Це псевдо — переконайся, що post.setUserId(...) приймає саме User або UserId
            var user = new User(); // імпортуй свою ентiті
            user.setUserId(dto.getUser().getId());
            post.setUserId(user);
        }

        return post;
    }


}