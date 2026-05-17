package com.sakute.project_fumo_backend.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
