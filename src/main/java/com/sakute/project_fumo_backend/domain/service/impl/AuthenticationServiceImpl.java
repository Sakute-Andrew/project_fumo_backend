package com.sakute.project_fumo_backend.domain.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakute.project_fumo_backend.controller.exeption.CustomDataNotFoundException;
import com.sakute.project_fumo_backend.domain.enteties.Token;
import com.sakute.project_fumo_backend.domain.enteties.TokenType;
import com.sakute.project_fumo_backend.domain.enteties.dto.AuthUserDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.request.LoginRequest;
import com.sakute.project_fumo_backend.domain.enteties.dto.request.RefreshToken;
import com.sakute.project_fumo_backend.domain.enteties.dto.request.RegisterRequest;
import com.sakute.project_fumo_backend.domain.enteties.dto.response.AuthenticationDto;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.auth.AuthenticationService;
import com.sakute.project_fumo_backend.domain.service.auth.JwtService;
import com.sakute.project_fumo_backend.repository.jpa_repo.TokenRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationDto login(LoginRequest request) {

        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Login request, email or password cannot be null");
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new CustomDataNotFoundException("User with email [" + request.getEmail() + "] not found")
                );

        if (user == null) {
            throw new CustomDataNotFoundException("User object is null after fetching by email");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        try {
            revokeAllUserTokens(user);
        } catch (CustomDataNotFoundException e) {
            log.error("Revoke all user tokens failed", e);
        }

        return saveUserTokenAndReturnAuthResponse(user);
    }


    @Override
    @Transactional
    public AuthenticationDto register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with email [" + request.getEmail() + "] or username [" + request.getUsername() + "] already exists.");
        }


        User user = userRepository.save(buildUser(request));
        return saveUserTokenAndReturnAuthResponse(user);
    }

    @Override
    public AuthenticationDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        ServletInputStream stream = request.getInputStream();

        String refreshToken = getRefreshTokenFromRequestBody(stream);

        String userEmail;

        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (ExpiredJwtException e) {
            Token tokenData = tokenRepository.findByRefreshTokenAndExpiredFalseAndRevokedFalse(refreshToken)
                    .orElseThrow(() -> new RuntimeException("The refresh token is not valid!"));

            userEmail = tokenData.getUser().getEmail();
        }

        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail).orElseThrow();

            boolean isRefreshTokenValid = jwtService.isTokenValid(refreshToken, user);

            if (!isRefreshTokenValid)
                refreshToken = jwtService.generateRefreshToken(user);

            String accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveToken(user, accessToken, refreshToken);

            return AuthenticationDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new IOException("Something went wrong");
    }

    private User buildUser(RegisterRequest request) {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(Role.USER);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        log.info("User " + user.getFullName() + " has been created");
        return user;
    }

    private AuthenticationDto saveUserTokenAndReturnAuthResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveToken(user, jwtToken, refreshToken);
        log.info("{} Generated JWT token", user.toString());
        return AuthenticationDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(new AuthUserDto(user.getUserId() ,user.getUsername(), user.getEmail(), user.getUserRole()))
                .build();
    }

    private void saveToken(User user, String jwtToken, String refreshToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user);

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private String getRefreshTokenFromRequestBody(ServletInputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RefreshToken myRefreshToken = objectMapper.readValue(stream, RefreshToken.class);
        return myRefreshToken.getRefreshToken();
    }
}
