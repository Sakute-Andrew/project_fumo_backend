package com.sakute.project_fumo_backend.controller.exeption;

public class NotAuthorizedExeption extends RuntimeException {
    public NotAuthorizedExeption(String message) {
        super(message);
    }

}
