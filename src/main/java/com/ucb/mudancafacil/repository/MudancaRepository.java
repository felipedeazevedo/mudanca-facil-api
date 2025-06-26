package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Mudanca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MudancaRepository extends JpaRepository<Mudanca, Long> {
    List<Mudanca> findByClienteId(Long clienteId);
}
