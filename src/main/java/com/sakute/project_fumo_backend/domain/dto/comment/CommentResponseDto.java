package com.sakute.project_fumo_backend.domain.dto.comment;

import com.sakute.project_fumo_backend.domain.dto.user.UserDto;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserDto user;
    private Timestamp createdAt;
    private Long parentCommentId;
    private List<CommentResponseDto> replies;
}
