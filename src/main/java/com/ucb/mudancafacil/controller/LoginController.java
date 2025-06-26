package com.ucb.mudancafacil.controller;

import com.ucb.mudancafacil.dto.LoginRequest;
import com.ucb.mudancafacil.dto.LoginResponse;
import com.ucb.mudancafacil.model.Cliente;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.ClienteRepository;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmailAndSenha(request.getEmail(), request.getSenha());
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            return ResponseEntity.ok(new LoginResponse("cliente", cliente.getId(), cliente.getNome()));
        }

        Optional<Empresa> empresaOpt = empresaRepository.findByEmailAndSenha(request.getEmail(), request.getSenha());
        if (empresaOpt.isPresent()) {
            Empresa empresa = empresaOpt.get();
            return ResponseEntity.ok(new LoginResponse("empresa", empresa.getId(), empresa.getNome()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
    }
}