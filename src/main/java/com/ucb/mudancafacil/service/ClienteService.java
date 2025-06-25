package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.ClienteDTO;
import com.ucb.mudancafacil.model.Cliente;
import com.ucb.mudancafacil.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<ClienteDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<ClienteDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public ClienteDTO salvar(ClienteDTO dto) {
        Cliente cliente = toEntity(dto);
        return toDTO(repository.save(cliente));
    }

    public Optional<ClienteDTO> atualizar(Long id, ClienteDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNome(dto.getNome());
            existing.setEmail(dto.getEmail());
            existing.setSenha(dto.getSenha());
            return toDTO(repository.save(existing));
        });
    }

    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setSenha(cliente.getSenha());
        return dto;
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha());
        return cliente;
    }
}
