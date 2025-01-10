package com.sakute.project_fumo_backend.controller.exeption;

public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException() {
        super("Operation not allowed");
    }
}
