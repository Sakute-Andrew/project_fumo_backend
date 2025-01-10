package com.sakute.project_fumo_backend.domain.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private Integer id;

    @NotBlank(message = "Comment can not be empty")
    @Max(value = 238, message = "Maximum characters used")
    private String content;

    private String author;

    private Date createdAt;

}
