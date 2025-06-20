package com.sakute.project_fumo_backend.domain.enteties.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class OrderDto {

    @NotNull
    private UUID orderId;

    @NotNull
    private AuthUserDto customer;

    @NotNull
    private UUID intellectualPropertyId;

    @NotNull
    private Timestamp orderDate;

    @NotNull
    private String orderStatus;

    private int price;

}
