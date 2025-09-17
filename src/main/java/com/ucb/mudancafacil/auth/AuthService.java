package com.ucb.mudancafacil.auth;

import com.ucb.mudancafacil.auth.dto.LoginRequest;
import com.ucb.mudancafacil.auth.dto.LoginResponse;
import com.ucb.mudancafacil.model.Cliente;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.ClienteRepository;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Hash BCrypt de uma senha dummy ("dummy") para mitigar enumeração por timing
    // Pode ser qualquer hash válido do mesmo algoritmo usado nos clientes reais
    private static final String DUMMY_BCRYPT = "$2a$10$7EqJtq98hPqEX7fNZaFWoOa7LUDh8YpQ4G5QWQ70c8p8K4D4hF8eS";

    public LoginResponse login(LoginRequest req) {
        final String email = normalizeEmail(req.getEmail());
        final String rawPwd = req.getSenha() == null ? "" : req.getSenha();

        var empresaOpt = empresaRepository.findByEmail(email);
        var clienteOpt = clienteRepository.findByEmail(email);

        if (empresaOpt.isPresent()) {
            boolean empresaPwdOk = passwordEncoder.matches(
                    rawPwd,
                    empresaOpt.map(Empresa::getSenhaHash).orElse(DUMMY_BCRYPT)
            );
            if (empresaPwdOk) {
                var emp = empresaOpt.get();
                String token = jwtService.generateToken(emp.getId(), emp.getEmail(), List.of("EMPRESA"));
                return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds());
            }
        }
        if (clienteOpt.isPresent()) {
            boolean clientePwdOk = passwordEncoder.matches(
                    rawPwd,
                    clienteOpt.map(Cliente::getSenhaHash).orElse(DUMMY_BCRYPT)
            );
            if (clientePwdOk) {
                var cli = clienteOpt.get();
                String token = jwtService.generateToken(cli.getId(), cli.getEmail(), List.of("CLIENTE"));
                return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds());
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
