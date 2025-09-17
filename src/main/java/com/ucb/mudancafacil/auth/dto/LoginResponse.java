package com.ucb.mudancafacil.auth.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
