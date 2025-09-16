package com.ucb.mudancafacil.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final Key key;
    @Getter
    private final long expirationSeconds;

    public JwtService(
            @Value("${security.jwt.secret-base64}") String secretB64,
            @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secretB64));
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(UUID empresaId, String email, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
                .setIssuer("mudanca-facil")
                .setAudience("api")
                .setId(UUID.randomUUID().toString())
                .setSubject(empresaId.toString())      // sub = ID
                .claim("email", email)                 // email como claim
                .claim("roles", roles)                 // lista de roles
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
