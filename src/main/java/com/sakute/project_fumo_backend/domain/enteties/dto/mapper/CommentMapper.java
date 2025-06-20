package com.sakute.project_fumo_backend.domain.enteties.dto.mapper;


import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.dto.CommentDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.response.CommentResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    /**
     * Конвертує CommentDto в Comment entity
     */
    public Comment mapToEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUserPostId(commentDto.getUserPostId());
        comment.setCreatedAt(Timestamp.from(Instant.now()));


        // Примітка: userId потрібно встановити окремо в сервісі,
        // оскільки потрібно завантажити User entity з бази даних

        return comment;
    }

    /**
     * Конвертує Comment entity в CommentResponseDto
     */
    public CommentResponseDto mapToResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(comment.getCommentId());
        responseDto.setContent(comment.getContent());
        responseDto.setCreatedAt(comment.getCreatedAt());

        // Маппінг користувача
        if (comment.getUserId() != null) {
            UserDto userDto = mapUserToDto(comment.getUserId());
            responseDto.setUser(userDto);
        }

        return responseDto;
    }

    /**
     * Конвертує список Comment entities в список CommentResponseDto
     */
    public List<CommentResponseDto> mapToResponseDtoList(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        return comments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Конвертує Comment entity назад в CommentDto (для редагування)
     */
    public CommentDto mapToDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        commentDto.setUserPostId(comment.getUserPostId());

        if (comment.getUserId() != null) {
            commentDto.setUserId(comment.getUserId().getUserId());
        }

        return commentDto;
    }

    /**
     * Допоміжний метод для маппінгу User в UserDto
     * Припускаю структуру UserDto на основі типових полів
     */
    private UserDto mapUserToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        // Припускаю, що UserDto має ці поля. Адаптуйте під вашу структуру
        userDto.setId(user.getUserId());
        userDto.setUsername(user.getUsername());
        // Додайте інші поля, які є в UserDto

        return userDto;
    }
}
