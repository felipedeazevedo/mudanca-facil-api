package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
