package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.EmpresaDTO;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository repository;

    public List<EmpresaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<EmpresaDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public EmpresaDTO salvar(EmpresaDTO dto) {
        Empresa empresa = toEntity(dto);
        return toDTO(repository.save(empresa));
    }

    public Optional<EmpresaDTO> atualizar(Long id, EmpresaDTO dto) {
        return repository.findById(id).map(existing -> {
            existing.setNome(dto.getNome());
            existing.setEmail(dto.getEmail());
            existing.setSenha(dto.getSenha());
            existing.setHorarioInicioDisponibilidade(dto.getHorarioInicioDisponibilidade());
            existing.setHorarioFimDisponibilidade(dto.getHorarioFimDisponibilidade());
            existing.setRaAtuacao(dto.getRaAtuacao());
            existing.setMediaPrecoMudancaPequena(dto.getMediaPrecoMudancaPequena());
            existing.setMediaPrecoMudancaMedia(dto.getMediaPrecoMudancaMedia());
            existing.setMediaPrecoMudancaGrande(dto.getMediaPrecoMudancaGrande());
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

    private EmpresaDTO toDTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNome(empresa.getNome());
        dto.setEmail(empresa.getEmail());
        dto.setSenha(empresa.getSenha());
        dto.setHorarioInicioDisponibilidade(empresa.getHorarioInicioDisponibilidade());
        dto.setHorarioFimDisponibilidade(empresa.getHorarioFimDisponibilidade());
        dto.setRaAtuacao(empresa.getRaAtuacao());
        dto.setMediaPrecoMudancaPequena(empresa.getMediaPrecoMudancaPequena());
        dto.setMediaPrecoMudancaMedia(empresa.getMediaPrecoMudancaMedia());
        dto.setMediaPrecoMudancaGrande(empresa.getMediaPrecoMudancaGrande());
        return dto;
    }

    private Empresa toEntity(EmpresaDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setEmail(dto.getEmail());
        empresa.setSenha(dto.getSenha());
        empresa.setHorarioInicioDisponibilidade(dto.getHorarioInicioDisponibilidade());
        empresa.setHorarioFimDisponibilidade(dto.getHorarioFimDisponibilidade());
        empresa.setRaAtuacao(dto.getRaAtuacao());
        empresa.setMediaPrecoMudancaPequena(dto.getMediaPrecoMudancaPequena());
        empresa.setMediaPrecoMudancaMedia(dto.getMediaPrecoMudancaMedia());
        empresa.setMediaPrecoMudancaGrande(dto.getMediaPrecoMudancaGrande());
        return empresa;
    }
}
