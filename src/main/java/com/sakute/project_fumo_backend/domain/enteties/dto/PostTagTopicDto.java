package com.sakute.project_fumo_backend.domain.enteties.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostTagTopicDto {

    private Integer id;

    @NotBlank(message = "Topic name can not be empty")
    private String name;
}
