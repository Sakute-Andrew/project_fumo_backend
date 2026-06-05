package com.sakute.project_fumo_backend.domain.dto.post;

import com.sakute.project_fumo_backend.domain.dto.user.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    // --- Сутність -> DTO (Віддаємо на фронтенд) ---
    @Mapping(source = "userPostId", target = "id")
    @Mapping(source = "postText", target = "postBody")
    @Mapping(source = "postDescription", target = "postDescription")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "topic", target = "postTopic")
    @Mapping(source = "author", target = "user")
    UserPostDto toDto(UserPost post);

    List<UserPostDto> toDtoList(List<UserPost> posts);

    default Page<UserPostDto> toDtoPage(Page<UserPost> postPage) {
        return postPage == null ? null : postPage.map(this::toDto);
    }

    // --- DTO -> Сутність (Зберігаємо в базу) ---
    @Mapping(source = "id", target = "userPostId")
    @Mapping(source = "postBody", target = "postText")
    @Mapping(source = "postDescription", target = "postDescription")
    @Mapping(source = "user", target = "author")
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt",
            defaultExpression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    UserPost toEntity(UserPostDto dto);

    // --- Оновлення існуючої сутності ---
    @Mapping(target = "userPostId", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(source = "postBody", target = "postText")
    @Mapping(source = "postDescription", target = "postDescription")
    void updateEntityFromDto(UserPostDto dto, @MappingTarget UserPost entity);


    @Mapping(source = "userId", target = "id")
    UserDto userToUserDto(User user);

    @Mapping(source = "id", target = "userId")
    User userDtoToUser(UserDto userDto);

    PostTopicDto toTopicDto(PostTagTopic topic);
}