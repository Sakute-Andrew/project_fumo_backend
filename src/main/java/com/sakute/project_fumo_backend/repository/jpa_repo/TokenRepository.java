package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Token;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findTokensByUserAndExpiredFalseAndRevokedFalse(User userId);

    Optional<Token> findByRefreshTokenAndExpiredFalseAndRevokedFalse(String refreshToken);

    Optional<Token> findByToken(String token);
}
