package com.sakute.project_fumo_backend.domain.service.auth;

import com.sakute.project_fumo_backend.domain.dto.auth.LoginRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.RegisterRequest;
import com.sakute.project_fumo_backend.domain.dto.auth.AuthenticationDto;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationDto login(LoginRequest request);

    AuthenticationDto register(RegisterRequest request);

    AuthenticationDto refreshToken(String request) throws IOException;
}
