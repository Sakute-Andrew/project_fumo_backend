package com.sakute.project_fumo_backend.controller.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.warn("🚫 Доступ заборонено до ресурсу: {}", request.getRequestURI());
        log.warn("Причина: {}", accessDeniedException.getMessage());

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
