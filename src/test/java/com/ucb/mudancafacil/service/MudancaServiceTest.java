package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.MudancaDTO;
import com.ucb.mudancafacil.enums.TipoMudanca;
import com.ucb.mudancafacil.model.Cliente;
import com.ucb.mudancafacil.model.Mudanca;
import com.ucb.mudancafacil.repository.ClienteRepository;
import com.ucb.mudancafacil.repository.MudancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MudancaServiceTest {

    @InjectMocks
    private MudancaService mudancaService;

    @Mock
    private MudancaRepository mudancaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Mudanca criarMudanca() {
        Mudanca m = new Mudanca();
        m.setId(1L);
        m.setOrigem("Rua A");
        m.setDestino("Rua B");
        m.setDataHoraMudanca(LocalDateTime.of(2025, 7, 1, 10, 0));
        m.setTipoMudanca(TipoMudanca.CASA);
        m.setCategoria("Pequena");

        Cliente cliente = new Cliente();
        cliente.setId(10L);

        m.setCliente(cliente);

        return m;
    }

    private MudancaDTO criarMudancaDTO() {
        MudancaDTO dto = new MudancaDTO();
        dto.setId(1L);
        dto.setOrigem("Rua A");
        dto.setDestino("Rua B");
        dto.setDataHoraMudanca(LocalDateTime.of(2025, 7, 1, 10, 0));
        dto.setTipoMudanca(TipoMudanca.CASA);
        dto.setCategoria("Pequena");
        dto.setClienteId(10L);
        return dto;
    }

    @Test
    void deveListarMudancas() {
        when(mudancaRepository.findAll()).thenReturn(List.of(criarMudanca()));

        List<MudancaDTO> mudancas = mudancaService.listar();

        assertEquals(1, mudancas.size());
        assertEquals("Rua A", mudancas.get(0).getOrigem());
        verify(mudancaRepository).findAll();
    }

    @Test
    void deveBuscarMudancaPorId_existente() {
        when(mudancaRepository.findById(1L)).thenReturn(Optional.of(criarMudanca()));

        Optional<MudancaDTO> dto = mudancaService.buscarPorId(1L);

        assertTrue(dto.isPresent());
        assertEquals("Rua B", dto.get().getDestino());
    }

    @Test
    void deveRetornarVazio_quandoMudancaNaoExistir() {
        when(mudancaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<MudancaDTO> dto = mudancaService.buscarPorId(999L);

        assertFalse(dto.isPresent());
    }

    @Test
    void deveSalvarMudanca() {
        Mudanca mudanca = criarMudanca();
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        when(mudancaRepository.save(any())).thenReturn(mudanca);

        MudancaDTO dto = criarMudancaDTO();
        dto.setClienteId(1L);

        MudancaDTO dtoSalvo = mudancaService.salvar(dto);

        assertEquals("Rua A", dtoSalvo.getOrigem());
        verify(mudancaRepository).save(any());
    }

    @Test
    void deveAtualizarMudanca_existente() {
        Mudanca existente = criarMudanca();
        when(mudancaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(mudancaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MudancaDTO novoDTO = criarMudancaDTO();
        novoDTO.setCategoria("Grande");
        novoDTO.setOrigem("Rua Nova");

        Optional<MudancaDTO> atualizado = mudancaService.atualizar(1L, novoDTO);

        assertTrue(atualizado.isPresent());
        assertEquals("Rua Nova", atualizado.get().getOrigem());
        assertEquals("Grande", atualizado.get().getCategoria());
    }

    @Test
    void naoDeveAtualizarMudancaInexistente() {
        when(mudancaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MudancaDTO> atualizado = mudancaService.atualizar(99L, criarMudancaDTO());

        assertFalse(atualizado.isPresent());
    }

    @Test
    void deveDeletarMudanca_existente() {
        when(mudancaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = mudancaService.deletar(1L);

        assertTrue(resultado);
        verify(mudancaRepository).deleteById(1L);
    }

    @Test
    void naoDeveDeletarMudancaInexistente() {
        when(mudancaRepository.existsById(123L)).thenReturn(false);

        boolean resultado = mudancaService.deletar(123L);

        assertFalse(resultado);
    }

    @Test
    void deveListarMudancasPorClienteId() {
        when(mudancaRepository.findByClienteId(10L)).thenReturn(List.of(criarMudanca()));

        List<MudancaDTO> resultado = mudancaService.listarPorClienteId(10L);

        assertEquals(1, resultado.size());
        assertEquals("Rua A", resultado.get(0).getOrigem());
        verify(mudancaRepository).findByClienteId(10L);
    }
}
