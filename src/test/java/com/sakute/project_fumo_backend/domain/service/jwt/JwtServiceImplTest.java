package com.sakute.project_fumo_backend.domain.service.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    private static final String TEST_SECRET =
            "c2VjcmV0LWtleS1mb3ItdGVzdGluZy1wdXJwb3Nlcy1vbmx5LTMyLWJ5dGVz";

    private static final long ACCESS_EXPIRATION  = 1000L * 60 * 60;      // 1 година
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 днів

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secretKey",             TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration",         ACCESS_EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", REFRESH_EXPIRATION);
    }

    // -------------------------------------------------------
    // generateToken / extractUsername
    // -------------------------------------------------------

    @Test
    void generateToken_shouldReturnNonEmptyString() {
        String token = jwtService.generateToken(userDetails("testuser"));
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        UserDetails user = userDetails("testuser");
        String token = jwtService.generateToken(user);

        String extracted = jwtService.extractUsername(token);

        assertThat(extracted).isEqualTo("testuser");
    }

    // -------------------------------------------------------
    // isTokenValid
    // -------------------------------------------------------

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenMatchesUser() {
        UserDetails user = userDetails("testuser");
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
        String token = jwtService.generateToken(userDetails("alice"));
        UserDetails otherUser = userDetails("bob");

        assertThat(jwtService.isTokenValid(token, otherUser)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() {
        // Виставляємо від'ємний expiration — токен вже протерміновано в момент створення
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);

        UserDetails user = userDetails("testuser");
        String expiredToken = jwtService.generateToken(user);

        // isTokenValid ловить JwtException і повертає false
        assertThat(jwtService.isTokenValid(expiredToken, user)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsGarbage() {
        UserDetails user = userDetails("testuser");

        assertThat(jwtService.isTokenValid("not.a.valid.token", user)).isFalse();
    }

    // -------------------------------------------------------
    // generateRefreshToken
    // -------------------------------------------------------

    @Test
    void generateRefreshToken_shouldReturnDifferentTokenThanAccessToken() {
        UserDetails user = userDetails("testuser");

        String accessToken  = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Різні терміни дії → різні токени
        assertThat(accessToken).isNotEqualTo(refreshToken);
    }

    @Test
    void generateRefreshToken_shouldContainCorrectUsername() {
        UserDetails user = userDetails("testuser");
        String refreshToken = jwtService.generateRefreshToken(user);

        assertThat(jwtService.extractUsername(refreshToken)).isEqualTo("testuser");
    }

    // -------------------------------------------------------
    // Хелпер
    // -------------------------------------------------------

    private UserDetails userDetails(String username) {
        UserDetails mock = mock(UserDetails.class);
        when(mock.getUsername()).thenReturn(username);
        return mock;
    }
}