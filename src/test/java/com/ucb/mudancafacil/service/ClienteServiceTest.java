package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.ClienteDTO;
import com.ucb.mudancafacil.model.Cliente;
import com.ucb.mudancafacil.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Cliente criarCliente() {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setNome("João");
        c.setEmail("joao@email.com");
        c.setSenha("123");
        return c;
    }

    private ClienteDTO criarClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1L);
        dto.setNome("João");
        dto.setEmail("joao@email.com");
        dto.setSenha("123");
        return dto;
    }

    @Test
    void deveListarClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(criarCliente()));

        List<ClienteDTO> clientes = clienteService.listar();

        assertEquals(1, clientes.size());
        assertEquals("João", clientes.get(0).getNome());
        verify(clienteRepository).findAll();
    }

    @Test
    void deveBuscarClientePorId_existente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(criarCliente()));

        Optional<ClienteDTO> dto = clienteService.buscarPorId(1L);

        assertTrue(dto.isPresent());
        assertEquals("João", dto.get().getNome());
    }

    @Test
    void deveRetornarVazio_quandoClienteNaoExistir() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<ClienteDTO> dto = clienteService.buscarPorId(2L);

        assertFalse(dto.isPresent());
    }

    @Test
    void deveSalvarCliente() {
        Cliente cliente = criarCliente();
        when(clienteRepository.save(any())).thenReturn(cliente);

        ClienteDTO dto = clienteService.salvar(criarClienteDTO());

        assertEquals("João", dto.getNome());
        verify(clienteRepository).save(any());
    }

    @Test
    void deveAtualizarCliente_existente() {
        Cliente existente = criarCliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ClienteDTO novoDTO = criarClienteDTO();
        novoDTO.setNome("Carlos");
        novoDTO.setEmail("Teste");
        novoDTO.setSenha("123");

        Optional<ClienteDTO> atualizado = clienteService.atualizar(1L, novoDTO);

        assertTrue(atualizado.isPresent());
        assertEquals("Carlos", atualizado.get().getNome());
        assertEquals("Teste", atualizado.get().getEmail());
        assertEquals("123", atualizado.get().getSenha());
    }

    @Test
    void naoDeveAtualizarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ClienteDTO> atualizado = clienteService.atualizar(99L, criarClienteDTO());

        assertFalse(atualizado.isPresent());
    }

    @Test
    void deveDeletarCliente_existente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        boolean result = clienteService.deletar(1L);

        assertTrue(result);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void naoDeveDeletarClienteInexistente() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        boolean result = clienteService.deletar(99L);

        assertFalse(result);
    }
}
