package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.MudancaDTO;
import com.ucb.mudancafacil.model.Mudanca;
import com.ucb.mudancafacil.repository.MudancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MudancaService {

    private final MudancaRepository repository;

    public List<MudancaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<MudancaDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public MudancaDTO salvar(MudancaDTO dto) {
        Mudanca mudanca = toEntity(dto);
        return toDTO(repository.save(mudanca));
    }

    public Optional<MudancaDTO> atualizar(Long id, MudancaDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setOrigem(dto.getOrigem());
            existing.setDestino(dto.getDestino());
            existing.setDataHoraMudanca(dto.getDataHoraMudanca());
            existing.setTipoMudanca(dto.getTipoMudanca());
            existing.setCategoria(dto.getCategoria());
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

    private MudancaDTO toDTO(Mudanca m) {
        MudancaDTO dto = new MudancaDTO();
        dto.setId(m.getId());
        dto.setOrigem(m.getOrigem());
        dto.setDestino(m.getDestino());
        dto.setDataHoraMudanca(m.getDataHoraMudanca());
        dto.setTipoMudanca(m.getTipoMudanca());
        dto.setCategoria(m.getCategoria());
        return dto;
    }

    private Mudanca toEntity(MudancaDTO dto) {
        Mudanca m = new Mudanca();
        m.setId(dto.getId()); // pode ser null no caso de POST
        m.setId(dto.getId());
        m.setOrigem(dto.getOrigem());
        m.setDestino(dto.getDestino());
        m.setDataHoraMudanca(dto.getDataHoraMudanca());
        m.setTipoMudanca(dto.getTipoMudanca());
        m.setCategoria(dto.getCategoria());
        return m;
    }
}
