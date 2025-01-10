package com.sakute.project_fumo_backend.controller.exeption;

public class FileSizeExeption extends RuntimeException {
    public FileSizeExeption(String message) {
        super(message);
    }
}
