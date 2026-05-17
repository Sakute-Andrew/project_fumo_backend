package com.sakute.project_fumo_backend.domain.dto.int_prop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class IntellectualPropertyDto {

    // Видалено ipId, бо при створенні він генерується базою, а при оновленні береться з URL
    // Але якщо він тобі дуже треба в DTO, залиши без NotNull
    private UUID ipId;

    @NotBlank(message = "Name is required") // Краще NotBlank для рядків
    @Size(max = 238, message = "Maximum characters used")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Maximum characters used")
    private String description;

    @NotBlank
    private String typeIp;

    @NotBlank
    private String userFullname;


    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank
    private String fileIp;

    @NotBlank
    private String status;


    @NotNull
    private String intellectualPropertyCategory;
}