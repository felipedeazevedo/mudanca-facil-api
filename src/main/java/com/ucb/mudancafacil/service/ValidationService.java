package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.repository.ClienteRepository;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;

    public void validateEmailUniqueness(String email) {
        if (clienteRepository.existsByEmail(email) || empresaRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }
    }
}
