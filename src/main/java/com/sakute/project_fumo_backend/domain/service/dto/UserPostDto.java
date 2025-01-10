package com.sakute.project_fumo_backend.domain.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class UserPostDto {

    private UUID userId;

    private String username;

    @NotBlank(message = "Post header can not be empty")
    @Max(value = 238, message = "Maximum characters used")
    private String postHeader;

    @NotBlank(message = "Post text can not be empty")
    @Max(value = 1000, message = "Maximum characters used")
    private String postBody;

    private Date postDate;

    @NotBlank(message = "You must select topic of post")
    private List<PostTagTopicDto> postTopic;

    private List<ImageDto> postImageBase64;

    private int likes;

    private int commentsCount;

    private boolean isPrivate;

    private String status;

}
