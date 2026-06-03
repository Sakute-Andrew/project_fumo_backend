package com.sakute.project_fumo_backend.domain.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.enteties.Token;
import com.sakute.project_fumo_backend.domain.enteties.TokenType;
import com.sakute.project_fumo_backend.domain.dto.auth.AuthUserDto;
import com.sakute.project_fumo_backend.domain.dto.auth.LoginRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.RefreshToken;
import com.sakute.project_fumo_backend.domain.dto.auth.RegisterRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.AuthenticationDto;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.user.UserProfiles;
import com.sakute.project_fumo_backend.domain.service.jwt.JwtService;
import com.sakute.project_fumo_backend.repository.jpa_repo.TokenRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserProfilesRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import jakarta.servlet.ServletInputStream;
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
    private final UserProfilesRepository userProfilesRepository;

    @Override
    @Transactional
    public AuthenticationDto login(LoginRequest request) {

        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Поля не можуть бути порожні");
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new NotFoundException("Користувача з емейлом " + request.getEmail() + " не існує")
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Викидаєш свій кастомний ексепшн, який на фронт поверне красивий статус (наприклад, 401 або 400)
            throw new InvalidInputException("Неправильний пароль");
        }

        if (user == null) {
            throw new NotFoundException("Об'єкт користувача порожній!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        try {
            revokeAllUserTokens(user);
        } catch (NotFoundException e) {
            log.error("Не вдалося відкликати всі токени", e);
        }

        return saveUserTokenAndReturnAuthResponse(user);
    }


    @Override
    @Transactional
    public AuthenticationDto register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Користувач з таким логіном вже існує!");
        }

        User user = userRepository.save(buildUser(request));

        UserProfiles profile = new UserProfiles();
        profile.setUser(user);
        userProfilesRepository.save(profile);

        return saveUserTokenAndReturnAuthResponse(user);
    }

    @Transactional
    public AuthenticationDto refreshToken(String refreshToken) {

        // 1. Дістаємо email.
        // Якщо токен протерміновано, цей метод САМ кине ExpiredJwtException.
        // Ми НЕ ловимо його тут. Твій GlobalExceptionHandler зловить його і поверне 403/401 на фронтенд.
        String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            throw new InvalidInputException("Некоректний токен"); // Або твій кастомний ексепшн
        }

        // 2. Шукаємо юзера
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено"));

        // 3. Перевіряємо, чи токен не відкликаний в базі (бо його могли вкрасти, і ти забанив його через БД)
        Token tokenData = tokenRepository.findByRefreshTokenAndExpiredFalseAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new OperationNotAllowedException("Токен оновлення відкликаний або не існує"));

        // 4. Перевіряємо криптографічну валідність
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new OperationNotAllowedException("Токен оновлення недійсний");
        }

        // 5. Усе супер! Генеруємо нову пару токенів (Rolling Refresh Tokens)
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // 6. Відкликаємо старі і зберігаємо нові
        revokeAllUserTokens(user);
        saveToken(user, newAccessToken, newRefreshToken);

        return AuthenticationDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private User buildUser(RegisterRequest request) {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        return user;
    }

    private AuthenticationDto saveUserTokenAndReturnAuthResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveToken(user, jwtToken, refreshToken);
        log.info("{} Generated JWT token", user.getUsername());
        return AuthenticationDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(AuthUserDto.builder()
                        .id(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .userRole(user.getRole())
                        .permissions(user.getPermissions())
                        .build())
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
