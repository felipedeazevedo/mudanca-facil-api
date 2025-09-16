package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.empresa.EmpresaCreateDTO;
import com.ucb.mudancafacil.dto.empresa.EmpresaListDTO;
import com.ucb.mudancafacil.dto.empresa.EmpresaUpdateDTO;
import com.ucb.mudancafacil.enums.StatusEmpresa;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private static final String EMPRESA_NOT_FOUND = "Empresa não encontrada";
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "razaoSocial", "status", "dataCriacao", "dataAtualizacao"
    );

    private final EmpresaRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EmpresaListDTO criar(EmpresaCreateDTO dto) {
        if (repository.existsByCnpj(dto.getCnpj())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado.");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }

        Empresa entity = new Empresa();
        entity.setCnpj(dto.getCnpj());
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setNomeResponsavel(dto.getNomeResponsavel());
        entity.setEmail(dto.getEmail());
        entity.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        entity.setTelefone(dto.getTelefone());
        entity.setStatus(StatusEmpresa.PENDING);
        entity.setDataCriacao(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")));
        entity.setDataAtualizacao(null);

        try {
            Empresa saved = repository.save(entity);
            return toListDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erro ao salvar empresa no banco de dados.");
        }
    }

    @Transactional
    public EmpresaListDTO patch(UUID id, EmpresaUpdateDTO dto) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EMPRESA_NOT_FOUND));

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(entity.getEmail())) {
            if (repository.existsByEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
            }
            entity.setEmail(dto.getEmail());
        }
        if (dto.getRazaoSocial() != null && !dto.getRazaoSocial().isBlank()) {
            entity.setRazaoSocial(dto.getRazaoSocial().trim());
        }
        if (dto.getNomeResponsavel() != null && !dto.getNomeResponsavel().isBlank()) {
            entity.setNomeResponsavel(dto.getNomeResponsavel().trim());
        }
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
            entity.setTelefone(dto.getTelefone().trim());
        }
        if (dto.getStatus() != null) {
            try {
                entity.setStatus(StatusEmpresa.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException iae) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Status inválido.");
            }
        }

        entity.setDataAtualizacao(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")));

        Empresa saved = repository.save(entity);
        return toListDTO(saved);
    }

    @Transactional
    public void deletar(UUID id) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EMPRESA_NOT_FOUND));
        // TODO: validar bloqueios de negócio (solicitações ativas, saldos, etc.)
        repository.delete(entity);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public EmpresaListDTO buscarPorId(UUID id) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EMPRESA_NOT_FOUND));
        return toListDTO(entity);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<EmpresaListDTO> listar(Pageable pageable) {
        Pageable safePagination = sanitize(pageable);
        return repository.findAll(safePagination).map(this::toListDTO);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public EmpresaListDTO buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sem e-mail.");
        }
        Empresa entity = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada para o token"));
        return toListDTO(entity);
    }

    private Pageable sanitize(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return PageRequest.of(0, 20, Sort.by("razaoSocial").ascending());
        }
        int size = Math.min(Math.max(pageable.getPageSize(), 1), 100); // 1..100
        List<Sort.Order> safeOrders = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            if (ALLOWED_SORT_FIELDS.contains(order.getProperty())) safeOrders.add(order);
        });
        Sort sort = safeOrders.isEmpty() ? Sort.by("razaoSocial").ascending() : Sort.by(safeOrders);
        return PageRequest.of(Math.max(pageable.getPageNumber(), 0), size, sort);
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
