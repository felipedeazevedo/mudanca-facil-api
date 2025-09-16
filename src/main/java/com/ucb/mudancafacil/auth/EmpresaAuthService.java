package com.ucb.mudancafacil.auth;

import com.ucb.mudancafacil.auth.dto.LoginRequest;
import com.ucb.mudancafacil.auth.dto.LoginResponse;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaAuthService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest req) {
        Empresa empresa = empresaRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Credenciais inválidas."));

        if (!passwordEncoder.matches(req.getSenha(), empresa.getSenhaHash())) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        String token = jwtService.generateToken(empresa.getId(), empresa.getEmail(), List.of("COMPANY"));
        return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }
}
