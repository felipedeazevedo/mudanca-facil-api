package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.cliente.ClienteCreateDTO;
import com.ucb.mudancafacil.dto.cliente.ClienteListDTO;
import com.ucb.mudancafacil.dto.cliente.ClienteUpdateDTO;
import com.ucb.mudancafacil.model.Cliente;
import com.ucb.mudancafacil.model.EnderecoCliente;
import com.ucb.mudancafacil.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private static final String CLIENTE_NOT_FOUND = "Cliente não encontrado.";

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClienteListDTO criar(ClienteCreateDTO dto) {
        String cpf = onlyDigits(dto.getCpf());
        String email = safeLower(dto.getEmail());

        if (repository.existsByCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado.");
        }
        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }

        Cliente entity = new Cliente();
        entity.setCpf(cpf);
        entity.setNome(safeTrim(dto.getNome()));
        entity.setEmail(email);
        entity.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        entity.setTelefone(safeTrim(dto.getTelefone()));

        EnderecoCliente end = new EnderecoCliente();
        var e = dto.getEndereco();
        end.setCep(onlyDigits(e.getCep()));
        end.setLogradouro(safeTrim(e.getLogradouro()));
        end.setNumero(safeTrim(e.getNumero()));
        end.setComplemento(safeTrim(e.getComplemento()));
        end.setBairro(safeTrim(e.getBairro()));
        end.setCidade(safeTrim(e.getCidade()));
        end.setUf(safeUpper(e.getUf()));
        entity.setEndereco(end);

        entity.setDataCriacao(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")));
        entity.setDataAtualizacao(null);

        try {
            Cliente saved = repository.save(entity);
            return toListDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Erro ao salvar cliente no banco de dados.");
        }
    }

    @Transactional
    public ClienteListDTO patch(UUID id, ClienteUpdateDTO dto) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CLIENTE_NOT_FOUND));

        if (dto.getEmail() != null) {
            String newEmail = safeLower(dto.getEmail());
            if (!newEmail.equalsIgnoreCase(entity.getEmail())) {
                if (repository.existsByEmail(newEmail)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
                }
                entity.setEmail(newEmail);
            }
        }
        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            entity.setNome(dto.getNome().trim());
        }
        if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
            entity.setTelefone(dto.getTelefone().trim());
        }

        if (dto.getEndereco() != null) {
            var e = dto.getEndereco();
            EnderecoCliente end = entity.getEndereco() != null ? entity.getEndereco() : new EnderecoCliente();

            if (e.getCep() != null) end.setCep(onlyDigits(e.getCep()));
            if (e.getLogradouro() != null) end.setLogradouro(safeTrim(e.getLogradouro()));
            if (e.getNumero() != null) end.setNumero(safeTrim(e.getNumero()));
            if (e.getComplemento() != null) end.setComplemento(safeTrim(e.getComplemento()));
            if (e.getBairro() != null) end.setBairro(safeTrim(e.getBairro()));
            if (e.getCidade() != null) end.setCidade(safeTrim(e.getCidade()));
            if (e.getUf() != null) end.setUf(safeUpper(e.getUf()));

            entity.setEndereco(end);
        }

        entity.setDataAtualizacao(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")));

        Cliente saved = repository.save(entity);
        return toListDTO(saved);
    }

    @Transactional
    public void deletar(UUID id) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CLIENTE_NOT_FOUND));
        // TODO: validar bloqueios de negócio, se houver
        repository.delete(entity);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public ClienteListDTO buscarPorId(UUID id) {
        Cliente entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CLIENTE_NOT_FOUND));
        return toListDTO(entity);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public ClienteListDTO buscarPorEmail(String emailRaw) {
        String email = safeLower(emailRaw);
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sem e-mail.");
        }
        Cliente entity = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para o token"));
        return toListDTO(entity);
    }

    private String onlyDigits(String s) {
        if (s == null) return null;
        return s.replaceAll("\\D", "");
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    private String safeLower(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    private String safeUpper(String s) {
        return s == null ? null : s.trim().toUpperCase();
    }

    private ClienteListDTO toListDTO(Cliente c) {
        ClienteListDTO dto = new ClienteListDTO();
        dto.setId(c.getId());
        dto.setCpfMascarado(maskCpf(c.getCpf()));
        dto.setNome(c.getNome());
        dto.setEmail(c.getEmail());
        dto.setTelefone(c.getTelefone());
        dto.setDataCriacao(c.getDataCriacao());
        dto.setDataAtualizacao(c.getDataAtualizacao());

        if (c.getEndereco() != null) {
            ClienteListDTO.EnderecoDTO e = new ClienteListDTO.EnderecoDTO();
            e.setCep(c.getEndereco().getCep());
            e.setLogradouro(c.getEndereco().getLogradouro());
            e.setNumero(c.getEndereco().getNumero());
            e.setComplemento(c.getEndereco().getComplemento());
            e.setBairro(c.getEndereco().getBairro());
            e.setCidade(c.getEndereco().getCidade());
            e.setUf(c.getEndereco().getUf());
            dto.setEndereco(e);
        }

        return dto;
    }

    private String maskCpf(String cpf11) {
        if (cpf11 == null || cpf11.length() != 11) return "***********";
        return "********" + cpf11.substring(8, 11); // mostra só os 3 últimos
    }
}
