package com.sakute.project_fumo_backend.domain.service.auth;

import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.dto.auth.*;
import com.sakute.project_fumo_backend.domain.enteties.EmailConfirmationToken;
import com.sakute.project_fumo_backend.domain.enteties.Token;
import com.sakute.project_fumo_backend.domain.enteties.TokenType;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.user.UserProfiles;
import com.sakute.project_fumo_backend.domain.service.email.EmailService;
import com.sakute.project_fumo_backend.domain.service.jwt.JwtService;
import com.sakute.project_fumo_backend.repository.jpa_repo.EmailConfirmationTokenRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.TokenRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserProfilesRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailConfirmationTokenRepository confirmationTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserProfilesRepository userProfilesRepository;
    private final EmailService emailService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public AuthenticationDto login(LoginRequest request) {

        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new InvalidInputException("Поля не можуть бути порожні");
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
    public RegistrationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidInputException("Користувач з таким email або іменем вже існує");
        }

        User user = userRepository.save(buildUser(request));

        UserProfiles profile = new UserProfiles();
        profile.setUser(user);
        userProfilesRepository.save(profile);

        sendConfirmationToken(user);

        return new RegistrationResponse(
                "Реєстрація успішна! Перевірте свою пошту та підтвердіть email.",
                user.getEmail()
        );
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        EmailConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Токен підтвердження не знайдено"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new OperationNotAllowedException("Email вже підтверджено");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OperationNotAllowedException("Термін дії токена підтвердження закінчився. Запросіть новий лист.");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        User user = confirmationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public RegistrationResponse resendConfirmation(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Користувача з email " + email + " не знайдено"));

        if (user.isEmailVerified()) {
            throw new OperationNotAllowedException("Email вже підтверджено");
        }

        confirmationTokenRepository.deleteByUser_UserId(user.getUserId());
        sendConfirmationToken(user);

        return new RegistrationResponse(
                "Лист з підтвердженням повторно надіслано.",
                user.getEmail()
        );
    }

    private void sendConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        EmailConfirmationToken confirmationToken = EmailConfirmationToken.builder()
                .id(UUID.randomUUID())
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        confirmationTokenRepository.save(confirmationToken);

        String confirmationLink = frontendUrl + "/auth/confirm?token=" + token;
        emailService.sendConfirmationEmail(user.getEmail(), user.getUsername(), confirmationLink);
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
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
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

}
