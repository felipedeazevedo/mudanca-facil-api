package com.ucb.mudancafacil.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final String accessToken;   // JWT
    private final String tokenType;     // "Bearer"
    private final long   expiresIn;     // em segundos
}
