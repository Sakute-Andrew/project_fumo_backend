package com.sakute.project_fumo_backend.controller.exeption;

public class NotFoundExeption extends RuntimeException {
    public NotFoundExeption(String message) {
        super(message);
    }

}
