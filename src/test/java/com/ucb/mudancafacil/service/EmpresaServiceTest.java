package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.EmpresaDTO;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Empresa criarEmpresa() {
        Empresa e = new Empresa();
        e.setId(1L);
        e.setNome("TransMudança");
        e.setEmail("contato@transmudanca.com");
        e.setSenha("segura123");
        e.setHorarioInicioDisponibilidade(LocalTime.of(8, 0));
        e.setHorarioFimDisponibilidade(LocalTime.of(18, 0));
        e.setRaAtuacao("DF");
        e.setMediaPrecoMudancaPequena(300.0);
        e.setMediaPrecoMudancaMedia(600.0);
        e.setMediaPrecoMudancaGrande(1000.0);
        return e;
    }

    private EmpresaDTO criarEmpresaDTO() {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(1L);
        dto.setNome("TransMudança");
        dto.setEmail("contato@transmudanca.com");
        dto.setSenha("segura123");
        dto.setHorarioInicioDisponibilidade(LocalTime.of(8, 0));
        dto.setHorarioFimDisponibilidade(LocalTime.of(18, 0));
        dto.setRaAtuacao("DF");
        dto.setMediaPrecoMudancaPequena(300.0);
        dto.setMediaPrecoMudancaMedia(600.0);
        dto.setMediaPrecoMudancaGrande(1000.0);
        return dto;
    }

    @Test
    void deveListarEmpresas() {
        when(empresaRepository.findAll()).thenReturn(List.of(criarEmpresa()));

        List<EmpresaDTO> empresas = empresaService.listar();

        assertEquals(1, empresas.size());
        assertEquals("TransMudança", empresas.get(0).getNome());
        verify(empresaRepository).findAll();
    }

    @Test
    void deveBuscarEmpresaPorId_existente() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(criarEmpresa()));

        Optional<EmpresaDTO> dto = empresaService.buscarPorId(1L);

        assertTrue(dto.isPresent());
        assertEquals("DF", dto.get().getRaAtuacao());
    }

    @Test
    void deveRetornarVazio_quandoEmpresaNaoExistir() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<EmpresaDTO> dto = empresaService.buscarPorId(99L);

        assertFalse(dto.isPresent());
    }

    @Test
    void deveSalvarEmpresa() {
        when(empresaRepository.save(any())).thenReturn(criarEmpresa());

        EmpresaDTO dto = empresaService.salvar(criarEmpresaDTO());

        assertEquals("TransMudança", dto.getNome());
        verify(empresaRepository).save(any());
    }

    @Test
    void deveAtualizarEmpresa_existente() {
        Empresa existente = criarEmpresa();
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(empresaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmpresaDTO dtoAtualizado = criarEmpresaDTO();
        dtoAtualizado.setNome("Nova Empresa");
        dtoAtualizado.setRaAtuacao("GO");

        Optional<EmpresaDTO> atualizado = empresaService.atualizar(1L, dtoAtualizado);

        assertTrue(atualizado.isPresent());
        assertEquals("Nova Empresa", atualizado.get().getNome());
        assertEquals("GO", atualizado.get().getRaAtuacao());
    }

    @Test
    void naoDeveAtualizarEmpresaInexistente() {
        when(empresaRepository.findById(123L)).thenReturn(Optional.empty());

        Optional<EmpresaDTO> atualizado = empresaService.atualizar(123L, criarEmpresaDTO());

        assertFalse(atualizado.isPresent());
    }

    @Test
    void deveDeletarEmpresa_existente() {
        when(empresaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = empresaService.deletar(1L);

        assertTrue(resultado);
        verify(empresaRepository).deleteById(1L);
    }

    @Test
    void naoDeveDeletarEmpresaInexistente() {
        when(empresaRepository.existsById(999L)).thenReturn(false);

        boolean resultado = empresaService.deletar(999L);

        assertFalse(resultado);
    }
}
