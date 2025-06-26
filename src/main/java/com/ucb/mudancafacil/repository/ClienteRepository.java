package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmailAndSenha(String email, String senha);
}
