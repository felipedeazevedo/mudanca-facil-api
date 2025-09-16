package com.ucb.mudancafacil.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtBeans {

    @Bean
    public JwtDecoder jwtDecoder(@Value("${security.jwt.secret-base64}") String secretB64) {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretB64);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * Converte o claim "roles" (lista) em GrantedAuthority usando prefixo ROLE_.
     * Se preferir "scope" (string com espaços), troque a configuração abaixo.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        var gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthoritiesClaimName("roles"); // ou "scope"
        gac.setAuthorityPrefix("ROLE_");

        var conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(gac);
        return conv;
    }
}
