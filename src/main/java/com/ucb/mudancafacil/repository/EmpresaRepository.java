package com.ucb.mudancafacil.repository;

import com.ucb.mudancafacil.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);
    Optional<Empresa> findByEmail(String email);
}
