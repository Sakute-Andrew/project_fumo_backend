package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.auth.*;
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
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Validated RegisterRequest request) {
        log.info("Register request: {}", request.getUsername());
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
        authenticationService.confirmEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<RegistrationResponse> resendConfirmation(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.resendConfirmation(email));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationDto> refreshToken(@RequestBody RefreshToken request) throws IOException {
        log.info("Отримано запит на оновлення токена");
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }
}
