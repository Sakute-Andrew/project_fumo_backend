package com.sakute.project_fumo_backend.domain.service.dto;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
public class CommentDto {

    private Long commentId;

    @NotBlank(message = "Comment can not be empty")
    @Max(value = 238, message = "Maximum characters used")
    private String content;

    private User userId;
    private Timestamp createdAt;
    private UUID userPostId;



}
