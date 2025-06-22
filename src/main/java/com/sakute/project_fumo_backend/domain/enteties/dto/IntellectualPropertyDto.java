package com.sakute.project_fumo_backend.domain.enteties.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class IntellectualPropertyDto {

    @NotNull
    private UUID ipId;

    @NotNull
    @Max(value = 238, message = "Maximum characters used")
    private String name;

    @NotNull
    @Max(value = 1000, message = "Maximum characters used")
    private String description;

    @NotNull
    private String typeIp;

    @NotNull
    private String userFullname;

    @NotNull
    private Timestamp createdAt;

    private BigDecimal price;

    @NotNull
    private String fileIp;

    @NotNull
    private String status;

    @NotNull
    private AuthUserDto owner;

    @NotNull
    private String intellectualPropertyCategory;


}
