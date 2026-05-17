package com.sakute.project_fumo_backend.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException(String args) {
        super("Operation not allowed");
    }
}
