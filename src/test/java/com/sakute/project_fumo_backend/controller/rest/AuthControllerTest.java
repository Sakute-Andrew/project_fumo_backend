package com.sakute.project_fumo_backend.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakute.project_fumo_backend.controller.exception.InvalidInputException;
import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.dto.auth.AuthenticationDto;
import com.sakute.project_fumo_backend.domain.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationService authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Ініціалізуємо валідатор щоб @Validated на LoginRequest/RegisterRequest працював
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService))
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
    }

    // -------------------------------------------------------
    // POST /api/v1/auth/login
    // -------------------------------------------------------

    @Test
    void login_shouldReturn200_whenCredentialsAreValid() throws Exception {
        AuthenticationDto response = AuthenticationDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "user@test.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access-token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh-token"));
    }

    @Test
    void login_shouldReturn400_whenEmailIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn400_whenPasswordIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "user@test.com",
                                  "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn400_whenEmailFormatIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "not-an-email",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------
    // POST /api/v1/auth/register
    // -------------------------------------------------------

    @Test
    void register_shouldReturn201_whenDataIsValid() throws Exception {
        AuthenticationDto response = AuthenticationDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "newuser@test.com",
                                  "username": "newuser",
                                  "full_name": "New User",
                                  "password": "Password1!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").value("access-token"));
    }

    @Test
    void register_shouldReturn400_whenUsernameIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "newuser@test.com",
                                  "username": "",
                                  "fullName": "New User",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------
    // POST /api/v1/auth/refresh-token
    // -------------------------------------------------------

    @Test
    void refreshToken_shouldReturn200_whenTokenIsValid() throws Exception {
        AuthenticationDto response = AuthenticationDto.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .build();

        when(authService.refreshToken(anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "valid-refresh-token"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("new-access-token"))
                .andExpect(jsonPath("$.refresh_token").value("new-refresh-token"));
    }

    @Test
    void refreshToken_shouldReturn500_whenTokenIsRevoked() throws Exception {
        // Без GlobalExceptionHandler Spring повертає 500 для кастомних ексепшнів.
        // Якщо у тебе є @ControllerAdvice який маппить OperationNotAllowedException → 403,
        // заміни status().isInternalServerError() на status().isForbidden()
        when(authService.refreshToken(anyString()))
                .thenThrow(new com.sakute.project_fumo_backend.controller.exception.OperationNotAllowedException("Токен відкликаний"));

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "revoked-token"
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}