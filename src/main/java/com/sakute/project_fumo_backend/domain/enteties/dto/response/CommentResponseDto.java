package com.sakute.project_fumo_backend.domain.enteties.dto.response;

import com.sakute.project_fumo_backend.domain.enteties.dto.UserDto;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserDto user;
    private Timestamp createdAt;
}
