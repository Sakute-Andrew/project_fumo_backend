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

    @Mapping(source = "userPostId", target = "id")
    @Mapping(source = "postDescription", target = "postBody")
    @Mapping(source = "createdAt", target = "postDate")
    @Mapping(source = "topic", target = "postTopic")
    @Mapping(source = "author", target = "user")
    @Mapping(target = "postText", ignore = true)  // якщо не потрібен
    UserPostDto toDto(UserPost post);

    List<UserPostDto> toDtoList(List<UserPost> posts);

    default Page<UserPostDto> toDtoPage(Page<UserPost> postPage) {
        return postPage == null ? null : postPage.map(this::toDto);
    }

    @Mapping(target = "userPostId", ignore = true)
    @Mapping(target = "author", ignore = true)             // було userId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "topic", ignore = true)              // було postTagTopic
    @Mapping(source = "postBody", target = "postDescription")
    void updateEntityFromDto(UserPostDto dto, @MappingTarget UserPost entity);

    @Mapping(source = "id", target = "userPostId")
    @Mapping(source = "postBody", target = "postDescription")
    @Mapping(source = "user", target = "author")           // було userId
    @Mapping(target = "topic", ignore = true)              // було postTagTopic
    @Mapping(target = "createdAt", source = "postDate",
            defaultExpression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    UserPost toEntity(UserPostDto dto);

    @Mapping(source = "userId", target = "id")
    UserDto userToUserDto(User user);

    @Mapping(source = "id", target = "userId")
    User userDtoToUser(UserDto userDto);

    // MapStruct сам знайде як конвертувати PostTagTopic -> PostTopicDto
// якщо додати окремий метод:
    PostTopicDto toTopicDto(PostTagTopic topic);
}