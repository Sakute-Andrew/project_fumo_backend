package com.sakute.project_fumo_backend.domain.dto.int_prop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class IntellectualPropertyDto {

    private UUID ipId;

    @NotBlank(message = "Name is required")
    @Size(max = 238, message = "Maximum characters used")
    private String name;

    private String description;

    @NotBlank(message = "Type is required")
    private String typeIp;

    private UUID ownerId;

    private String userFullname;

    private Timestamp createdAt;

    private String fileIp;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName;
}