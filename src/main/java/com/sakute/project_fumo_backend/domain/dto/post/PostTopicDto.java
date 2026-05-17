package com.sakute.project_fumo_backend.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto/post/PostTopicDto.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostTopicDto {
    private Long postTopicId;
    private String postName;
}