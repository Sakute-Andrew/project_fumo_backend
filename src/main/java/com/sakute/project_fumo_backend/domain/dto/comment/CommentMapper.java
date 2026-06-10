package com.sakute.project_fumo_backend.domain.dto.comment;


import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.dto.user.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "id", source = "commentId")
    @Mapping(target = "user", source = "author")
    @Mapping(target = "parentCommentId", source = "parentComment.commentId")
    @Mapping(target = "replies", source = "replies")
    CommentResponseDto toResponseDto(Comment comment);

    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt",
             expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
    Comment toEntity(CommentDto dto);

    @Mapping(target = "userPostId", source = "post.userPostId")
    @Mapping(target = "parentCommentId", source = "parentComment.commentId")
    CommentDto toDto(Comment comment);

    List<CommentResponseDto> toResponseDtoList(List<Comment> comments);

    default UserDto userToDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
