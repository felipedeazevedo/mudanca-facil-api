package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
