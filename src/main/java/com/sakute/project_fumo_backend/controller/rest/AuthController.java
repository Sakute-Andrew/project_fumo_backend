package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.auth.LoginRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.RefreshToken;
import com.sakute.project_fumo_backend.domain.dto.auth.RegisterRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.AuthenticationDto;
import com.sakute.project_fumo_backend.domain.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@RequestBody @Validated LoginRequest request) {
        log.info("Login request: {}", request);
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto> register(@RequestBody @Validated RegisterRequest request) {
        log.info("Register request: {}", request.getUsername());
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationDto> refreshToken(@RequestBody RefreshToken request) throws IOException {
        log.info("Отримано запит на оновлення токена");
        // Передаємо тільки рядок з токеном, ніяких HttpServletRequest!
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }
}
