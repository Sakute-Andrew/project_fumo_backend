package com.sakute.project_fumo_backend.domain.enteties.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("userId")
    private UUID userId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("userPostId")
    private UUID userPostId;

}
