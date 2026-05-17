package com.sakute.project_fumo_backend.controller.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        log.error("🔍 Ресурс не знайдено: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Object> handleOperationNotAllowed(OperationNotAllowedException ex) {
        log.error("🚫 Операція заборонена: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInput(InvalidInputException ex) {
        log.warn("⚠️ Некоректні вхідні дані: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> unauthorizedException(HttpClientErrorException.Unauthorized ex) {
        log.warn("⚠️ Неавторизований доступ: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        log.warn("⏳ JWT Токен протерміновано: {}", ex.getMessage());

        // Повертаємо 401 Unauthorized.
        // Це сигнал для фронтенду: "Очисть локал сторадж і покажи вікно логіну"
        return buildResponse(HttpStatus.UNAUTHORIZED, "Час дії вашої сесії минув. Будь ласка, увійдіть знову.");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleGenericJwtException(JwtException ex) {
        log.warn("🚫 Невалідний або підроблений JWT токен: {}", ex.getMessage());

        // Якщо токен зламаний, підроблений або не має підпису
        return buildResponse(HttpStatus.UNAUTHORIZED, "Недійсний токен авторизації.");
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
