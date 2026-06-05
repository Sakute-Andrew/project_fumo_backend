package com.sakute.project_fumo_backend.domain.dto.post;

import com.sakute.project_fumo_backend.domain.dto.user.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDto {

    private UUID id;

    @NotBlank(message = "Post header can not be empty")
    @Size(max = 300, message = "Maximum characters used")
    private String postHeader;

    @NotBlank(message = "Post text can not be empty")
    private String postBody;

    private String postDescription;

    private Timestamp createdAt;

    @NotNull(message = "You must select topic of post")
    private PostTopicDto postTopic;

    private String photo;

    private UserDto user;
}