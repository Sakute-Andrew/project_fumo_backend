package com.sakute.project_fumo_backend.controller.advice;

import com.sakute.project_fumo_backend.controller.exeption.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ExeptionAdvice {

    @ExceptionHandler(NotFoundExeption.class)
    public ResponseEntity<Error> handleNotFoundExeption(NotFoundExeption exeption, WebRequest request) {
        Error error = new Error();
        error.errorBuilder(HttpStatus.NOT_FOUND.value(), exeption.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Error> handleInvalidInput(InvalidInputException exeption, WebRequest request) {
        Error error = new Error();
        error.errorBuilder(HttpStatus.BAD_REQUEST.value(), exeption.getMessage(), new Date());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
