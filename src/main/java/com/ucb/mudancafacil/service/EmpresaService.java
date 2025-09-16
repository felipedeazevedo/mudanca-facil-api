package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.EmpresaCreateDTO;
import com.ucb.mudancafacil.dto.EmpresaListDTO;
import com.ucb.mudancafacil.dto.EmpresaUpdateDTO;
import com.ucb.mudancafacil.enums.StatusEmpresa;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EmpresaListDTO criar(EmpresaCreateDTO dto) {
        if (repository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado.");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Empresa entity = new Empresa();
        entity.setCnpj(dto.getCnpj());
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setNomeResponsavel(dto.getNomeResponsavel());
        entity.setEmail(dto.getEmail());
        entity.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        entity.setTelefone(dto.getTelefone());
        entity.setStatus(StatusEmpresa.PENDING);

        Empresa saved = repository.save(entity);
        return toListDTO(saved);
    }

    @Transactional
    public EmpresaListDTO patch(UUID id, EmpresaUpdateDTO dto) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(entity.getEmail())) {
            if (repository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
            entity.setEmail(dto.getEmail());
        }

        if (dto.getRazaoSocial() != null) {
            entity.setRazaoSocial(dto.getRazaoSocial());
        }
        if (dto.getNomeResponsavel() != null) {
            entity.setNomeResponsavel(dto.getNomeResponsavel());
        }
        if (dto.getTelefone() != null) {
            entity.setTelefone(dto.getTelefone());
        }
        if (dto.getStatus() != null) {
            StatusEmpresa novo = StatusEmpresa.valueOf(dto.getStatus());
            entity.setStatus(novo);
        }

        Empresa saved = repository.save(entity);
        return toListDTO(saved);
    }

    @Transactional
    public void deletar(UUID id) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        // TODO: aqui você pode checar bloqueios: solicitações ativas, saldos a receber, etc.
        repository.delete(entity);
    }

    public EmpresaListDTO buscarPorId(UUID id) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        return toListDTO(entity);
    }

    public List<EmpresaListDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toListDTO)
                .toList();
    }

    public EmpresaListDTO buscarPorEmail(String email) {
        return toListDTO(repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o token")));
    }

    private EmpresaListDTO toListDTO(Empresa e) {
        EmpresaListDTO dto = new EmpresaListDTO();
        dto.setId(e.getId());
        dto.setCnpjMascarado(maskCnpj(e.getCnpj()));
        dto.setRazaoSocial(e.getRazaoSocial());
        dto.setNomeResponsavel(e.getNomeResponsavel());
        dto.setEmail(e.getEmail());
        dto.setTelefone(e.getTelefone());
        dto.setStatus(e.getStatus().name());
        dto.setDataCriacao(e.getDataCriacao());
        dto.setDataAtualizacao(e.getDataAtualizacao());
        return dto;
    }

    private String maskCnpj(String cnpj14) {
        if (cnpj14 == null || cnpj14.length() != 14) return "**************";
        return "************" + cnpj14.substring(10, 14);
    }
}
