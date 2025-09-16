package com.ucb.mudancafacil.auth;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthConverter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/error").permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/empresas").permitAll()
                        .requestMatchers(HttpMethod.POST, "/clientes").permitAll()
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/empresas/me").hasRole("EMPRESA")
                        .requestMatchers("/empresas/**").hasRole("EMPRESA")
                        .requestMatchers("/clientes/me").hasRole("CLIENTE")
                        .requestMatchers("/clientes/**").hasRole("CLIENTE")

                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                // usando JWT, mantém; se não, remover este bloco
                .oauth2ResourceServer(oauth2 -> oauth2
                        // .bearerTokenResolver(bearerTokenResolver)
                        .jwt(j -> j.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    BearerTokenResolver bearerTokenResolver() {
//        DefaultBearerTokenResolver delegate = new DefaultBearerTokenResolver();
//        return request -> {
//            String ctx = request.getContextPath(); // considera context-path (/api, etc.)
//            String uri = request.getRequestURI();
//
//            boolean isPublicPost =
//                    ("POST".equals(request.getMethod()) &&
//                            ( (ctx + "/empresas").equals(uri) ||
//                                    (ctx + "/clientes").equals(uri) ));   // 👈 inclua aqui outras públicas
//
//            if (isPublicPost) {
//                return null; // ignora bearer neste endpoint
//            }
//            return delegate.resolve(request);
//        };
//    }
}
