package com.sakute.project_fumo_backend.domain.enteties.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sakute.project_fumo_backend.domain.enteties.dto.AuthUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("user")
    private AuthUserDto user;
}
