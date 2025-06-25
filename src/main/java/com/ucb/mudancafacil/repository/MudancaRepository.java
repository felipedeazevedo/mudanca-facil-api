package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Mudanca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MudancaRepository extends JpaRepository<Mudanca, Long> {
}
