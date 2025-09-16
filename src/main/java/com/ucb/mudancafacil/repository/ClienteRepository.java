package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
}
