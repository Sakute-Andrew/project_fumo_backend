package com.sakute.project_fumo_backend.domain.dto.comment;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentDto {
    private String content;
    private UUID userPostId;
}
