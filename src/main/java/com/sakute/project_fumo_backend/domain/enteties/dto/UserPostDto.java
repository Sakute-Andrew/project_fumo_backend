package com.sakute.project_fumo_backend.domain.enteties.dto;

import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserPostDto {

    private UUID id;

    @NotBlank(message = "Post header can not be empty")
    @Max(value = 238, message = "Maximum characters used")
    private String postHeader;

    @NotBlank(message = "Post text can not be empty")
    private String postBody;

    private String postText;

    private Date postDate;

    @NotBlank(message = "You must select topic of post")
    private PostTagTopic postTopic;

    private String photo;

    private UserDto user;




}
