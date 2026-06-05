package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException;
import com.sakute.project_fumo_backend.domain.dto.auth.AuthenticationDto;
import com.sakute.project_fumo_backend.domain.dto.auth.LoginRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.RegisterRequest;
import com.sakute.project_fumo_backend.domain.enteties.Token;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.user.UserProfiles;
import com.sakute.project_fumo_backend.domain.service.auth.AuthenticationServiceImpl;
import com.sakute.project_fumo_backend.domain.service.jwt.JwtService;
import com.sakute.project_fumo_backend.repository.jpa_repo.TokenRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserProfilesRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserProfilesRepository userProfilesRepository;

    @InjectMocks
    private AuthenticationServiceImpl authService;

    // -------------------------------------------------------
    // login — валідація вхідних даних
    // -------------------------------------------------------

    @Test
    void login_shouldThrow_whenRequestIsNull() {
        assertThatThrownBy(() -> authService.login(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("порожні");
    }

    @Test
    void login_shouldThrow_whenEmailIsNull() {
        LoginRequest request = loginRequest(null, "password");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void login_shouldThrow_whenPasswordIsNull() {
        LoginRequest request = loginRequest("user@test.com", null);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // -------------------------------------------------------
    // login — бізнес-логіка
    // -------------------------------------------------------

    @Test
    void login_shouldThrow_whenUserNotFound() {
        LoginRequest request = loginRequest("ghost@test.com", "pass");
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ghost@test.com");
    }

    @Test
    void login_shouldThrow_whenPasswordIncorrect() {
        LoginRequest request = loginRequest("user@test.com", "wrong");
        User user = userWithPassword("encodedPassword");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("пароль");
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsCorrect() {
        LoginRequest request = loginRequest("user@test.com", "correct");
        User user = userWithPassword("encodedPassword");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct", "encodedPassword")).thenReturn(true);
        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)).thenReturn(List.of());
        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(tokenRepository.save(any())).thenReturn(new Token());

        AuthenticationDto result = authService.login(request);

        assertThat(result.getAccessToken()).isEqualTo("access-token");
        assertThat(result.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void login_shouldRevokeExistingTokens_whenUserLogsIn() {
        LoginRequest request = loginRequest("user@test.com", "correct");
        User user = userWithPassword("encodedPassword");
        Token existingToken = new Token();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct", "encodedPassword")).thenReturn(true);
        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user))
                .thenReturn(List.of(existingToken));
        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(tokenRepository.save(any())).thenReturn(new Token());

        authService.login(request);

        assertThat(existingToken.isRevoked()).isTrue();
        assertThat(existingToken.isExpired()).isTrue();
        verify(tokenRepository).saveAll(List.of(existingToken));
    }

    // -------------------------------------------------------
    // register
    // -------------------------------------------------------

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        RegisterRequest request = registerRequest("existing@test.com", "newuser");
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("вже існує");
    }

    @Test
    void register_shouldThrow_whenUsernameAlreadyExists() {
        RegisterRequest request = registerRequest("new@test.com", "takenuser");
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("takenuser")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("вже існує");
    }

    @Test
    void register_shouldSaveUserAndProfile_whenDataIsValid() {
        RegisterRequest request = registerRequest("new@test.com", "newuser");
        request.setPassword("password");
        request.setFullName("New User");

        User savedUser = new User();
        savedUser.setUsername("newuser");

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(savedUser)).thenReturn("refresh-token");
        when(tokenRepository.save(any())).thenReturn(new Token());

        AuthenticationDto result = authService.register(request);

        assertThat(result.getAccessToken()).isEqualTo("access-token");
        verify(userProfilesRepository).save(any(UserProfiles.class));
    }

    // -------------------------------------------------------
    // refreshToken
    // -------------------------------------------------------

    @Test
    void refreshToken_shouldThrow_whenUsernameIsNull() {
        when(jwtService.extractUsername("badtoken")).thenReturn(null);

        assertThatThrownBy(() -> authService.refreshToken("badtoken"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Некоректний токен");
    }

    @Test
    void refreshToken_shouldThrow_whenUserNotFound() {
        when(jwtService.extractUsername("token")).thenReturn("ghost");
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken("token"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void refreshToken_shouldThrow_whenTokenRevoked() {
        User user = new User();
        when(jwtService.extractUsername("revokedtoken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(tokenRepository.findByRefreshTokenAndExpiredFalseAndRevokedFalse("revokedtoken"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken("revokedtoken"))
                .isInstanceOf(OperationNotAllowedException.class)
                .hasMessageContaining("відкликаний");
    }

    @Test
    void refreshToken_shouldThrow_whenTokenCryptographicallyInvalid() {
        User user = new User();
        Token tokenData = new Token();
        when(jwtService.extractUsername("invalidtoken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(tokenRepository.findByRefreshTokenAndExpiredFalseAndRevokedFalse("invalidtoken"))
                .thenReturn(Optional.of(tokenData));
        when(jwtService.isTokenValid("invalidtoken", user)).thenReturn(false);

        assertThatThrownBy(() -> authService.refreshToken("invalidtoken"))
                .isInstanceOf(OperationNotAllowedException.class)
                .hasMessageContaining("недійсний");
    }

    @Test
    void refreshToken_shouldReturnNewTokenPair_whenEverythingIsValid() {
        User user = new User();
        Token tokenData = new Token();
        when(jwtService.extractUsername("validtoken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(tokenRepository.findByRefreshTokenAndExpiredFalseAndRevokedFalse("validtoken"))
                .thenReturn(Optional.of(tokenData));
        when(jwtService.isTokenValid("validtoken", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("new-refresh-token");
        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)).thenReturn(List.of());
        when(tokenRepository.save(any())).thenReturn(new Token());

        AuthenticationDto result = authService.refreshToken("validtoken");

        assertThat(result.getAccessToken()).isEqualTo("new-access-token");
        assertThat(result.getRefreshToken()).isEqualTo("new-refresh-token");
    }

    // -------------------------------------------------------
    // Хелпери
    // -------------------------------------------------------

    private LoginRequest loginRequest(String email, String password) {
        LoginRequest r = new LoginRequest();
        r.setEmail(email);
        r.setPassword(password);
        return r;
    }

    private RegisterRequest registerRequest(String email, String username) {
        RegisterRequest r = new RegisterRequest();
        r.setEmail(email);
        r.setUsername(username);
        return r;
    }

    private User userWithPassword(String encodedPassword) {
        User user = new User();
        user.setPassword(encodedPassword);
        return user;
    }
}