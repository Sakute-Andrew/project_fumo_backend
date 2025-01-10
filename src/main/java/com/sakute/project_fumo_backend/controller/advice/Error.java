package com.sakute.project_fumo_backend.controller.advice;

import lombok.Data;

import java.util.Date;

@Data
public class Error {
    private Integer statusCode;
    private String message;
    private Date timestamp;

    public Error errorBuilder(Integer statusCode, String message, Date timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
        return this;
    }
}
