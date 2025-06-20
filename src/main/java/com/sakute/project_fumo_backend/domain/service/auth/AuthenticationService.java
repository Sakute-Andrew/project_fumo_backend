package com.sakute.project_fumo_backend.domain.service.auth;

import com.sakute.project_fumo_backend.domain.enteties.dto.request.LoginRequest;
import com.sakute.project_fumo_backend.domain.enteties.dto.request.RegisterRequest;
import com.sakute.project_fumo_backend.domain.enteties.dto.response.AuthenticationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationDto login(LoginRequest request);

    AuthenticationDto register(RegisterRequest request);

    AuthenticationDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
