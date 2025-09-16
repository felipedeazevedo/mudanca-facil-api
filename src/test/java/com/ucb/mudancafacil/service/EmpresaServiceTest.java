package com.ucb.mudancafacil.service;

import com.ucb.mudancafacil.dto.empresa.EmpresaCreateDTO;
import com.ucb.mudancafacil.dto.empresa.EmpresaListDTO;
import com.ucb.mudancafacil.dto.empresa.EmpresaUpdateDTO;
import com.ucb.mudancafacil.enums.StatusEmpresa;
import com.ucb.mudancafacil.model.Empresa;
import com.ucb.mudancafacil.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmpresaService service;

    private EmpresaCreateDTO createDTO;
    private EmpresaUpdateDTO updateDTO;
    private Empresa empresaPersistida;
    private UUID id;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();

        createDTO = new EmpresaCreateDTO();
        createDTO.setCnpj("12345678000190");
        createDTO.setRazaoSocial("ACME LTDA");
        createDTO.setNomeResponsavel("Wile Coyote");
        createDTO.setEmail("contato@acme.com");
        createDTO.setSenha("segredo123");
        createDTO.setTelefone("+5561999990000");

        updateDTO = new EmpresaUpdateDTO();
        updateDTO.setRazaoSocial("ACME DO BRASIL");
        updateDTO.setNomeResponsavel("Pernalonga");
        updateDTO.setTelefone("+5561000009999");
        // email e status setados nos testes conforme cenário

        empresaPersistida = new Empresa();
        empresaPersistida.setId(id);
        empresaPersistida.setCnpj(createDTO.getCnpj());
        empresaPersistida.setRazaoSocial(createDTO.getRazaoSocial());
        empresaPersistida.setNomeResponsavel(createDTO.getNomeResponsavel());
        empresaPersistida.setEmail(createDTO.getEmail());
        empresaPersistida.setSenhaHash("$2a$10$hash"); // simulado
        empresaPersistida.setTelefone(createDTO.getTelefone());
        empresaPersistida.setStatus(StatusEmpresa.PENDING);
        empresaPersistida.setDataCriacao(OffsetDateTime.now());
        empresaPersistida.setDataAtualizacao(OffsetDateTime.now());
    }

    // ---------- CRIAR ----------
    @Nested
    class Criar {

        @Test
        @DisplayName("Deve criar empresa quando CNPJ e e-mail não existem")
        void criar_ok() {
            when(repository.existsByCnpj(createDTO.getCnpj())).thenReturn(false);
            when(repository.existsByEmail(createDTO.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(createDTO.getSenha())).thenReturn("ENCODED");
            // Capturar entidade enviada ao save
            ArgumentCaptor<Empresa> captor = ArgumentCaptor.forClass(Empresa.class);
            when(repository.save(any(Empresa.class))).thenAnswer(inv -> {
                Empresa e = inv.getArgument(0, Empresa.class);
                e.setId(id);
                e.setDataCriacao(OffsetDateTime.now());
                e.setDataAtualizacao(OffsetDateTime.now());
                return e;
            });

            EmpresaListDTO dto = service.criar(createDTO);

            verify(repository).existsByCnpj(createDTO.getCnpj());
            verify(repository).existsByEmail(createDTO.getEmail());
            verify(passwordEncoder).encode(createDTO.getSenha());
            verify(repository).save(captor.capture());

            Empresa salvo = captor.getValue();
            assertThat(salvo.getSenhaHash()).isEqualTo("ENCODED");
            assertThat(salvo.getStatus()).isEqualTo(StatusEmpresa.PENDING);

            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getEmail()).isEqualTo(createDTO.getEmail());
            assertThat(dto.getCnpjMascarado()).endsWith("0190");
        }

        @Test
        @DisplayName("Deve falhar se CNPJ já existir")
        void criar_cnpjDuplicado() {
            when(repository.existsByCnpj(createDTO.getCnpj())).thenReturn(true);

            assertThatThrownBy(() -> service.criar(createDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("CNPJ já cadastrado");

            verify(repository, never()).existsByEmail(any());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve falhar se e-mail já existir")
        void criar_emailDuplicado() {
            when(repository.existsByCnpj(createDTO.getCnpj())).thenReturn(false);
            when(repository.existsByEmail(createDTO.getEmail())).thenReturn(true);

            assertThatThrownBy(() -> service.criar(createDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("E-mail já cadastrado");

            verify(repository, never()).save(any());
        }
    }

    // ---------- PATCH ----------
    @Nested
    class Patch {

        @Test
        @DisplayName("Deve atualizar campos parciais sem alterar e-mail quando não mudou")
        void patch_parcial_semMudarEmail() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));
            when(repository.save(any(Empresa.class))).thenAnswer(inv -> inv.getArgument(0));

            // email permanece igual → não deve checar existsByEmail
            updateDTO.setEmail(empresaPersistida.getEmail());

            EmpresaListDTO dto = service.patch(id, updateDTO);

            verify(repository, never()).existsByEmail(anyString());
            assertThat(dto.getRazaoSocial()).isEqualTo("ACME DO BRASIL");
            assertThat(dto.getNomeResponsavel()).isEqualTo("Pernalonga");
            assertThat(dto.getTelefone()).isEqualTo("+5561000009999");
        }

        @Test
        @DisplayName("Deve atualizar e-mail quando alterado e não duplicado")
        void patch_mudaEmail_ok() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));
            when(repository.existsByEmail("novo@acme.com")).thenReturn(false);
            when(repository.save(any(Empresa.class))).thenAnswer(inv -> inv.getArgument(0));

            updateDTO.setEmail("novo@acme.com");

            EmpresaListDTO dto = service.patch(id, updateDTO);

            verify(repository).existsByEmail("novo@acme.com");
            assertThat(dto.getEmail()).isEqualTo("novo@acme.com");
        }

        @Test
        @DisplayName("Deve falhar ao alterar e-mail para um já existente")
        void patch_mudaEmail_duplicado() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));
            when(repository.existsByEmail("jaexiste@acme.com")).thenReturn(true);

            updateDTO.setEmail("jaexiste@acme.com");

            assertThatThrownBy(() -> service.patch(id, updateDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("E-mail já cadastrado");

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve atualizar status quando string válida")
        void patch_status_valido() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));
            when(repository.save(any(Empresa.class))).thenAnswer(inv -> inv.getArgument(0));

            updateDTO.setStatus("ACTIVE");

            EmpresaListDTO dto = service.patch(id, updateDTO);

            assertThat(dto.getStatus()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("Deve lançar EntityNotFound se empresa não existir no patch")
        void patch_naoEncontrada() {
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.patch(id, updateDTO))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }

        @Test
        @DisplayName("Deve falhar com IllegalArgumentException se status inválido")
        void patch_status_invalido() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));
            updateDTO.setStatus("NAO_EXISTE");

            assertThatThrownBy(() -> service.patch(id, updateDTO))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ---------- DELETE ----------
    @Nested
    class Delete {

        @Test
        @DisplayName("Deve deletar empresa existente")
        void deletar_ok() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));
            doNothing().when(repository).delete(empresaPersistida);

            service.deletar(id);

            verify(repository).delete(empresaPersistida);
        }

        @Test
        @DisplayName("Deve lançar EntityNotFound se empresa não existir no delete")
        void deletar_naoEncontrada() {
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deletar(id))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }
    }

    // ---------- BUSCAR / LISTAR ----------
    @Nested
    class BuscarListar {

        @Test
        @DisplayName("Deve buscar por id e retornar DTO")
        void buscarPorId_ok() {
            when(repository.findById(id)).thenReturn(Optional.of(empresaPersistida));

            EmpresaListDTO dto = service.buscarPorId(id);

            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getEmail()).isEqualTo(empresaPersistida.getEmail());
            assertThat(dto.getStatus()).isEqualTo(empresaPersistida.getStatus().name());
        }

        @Test
        @DisplayName("Deve lançar EntityNotFound se não existir no buscarPorId")
        void buscarPorId_naoEncontrada() {
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.buscarPorId(id))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Empresa não encontrada");
        }

//        @Test
//        @DisplayName("Deve listar e aplicar máscara de CNPJ corretamente")
//        void listar_mascaraCnpj() {
//            Empresa e1 = clone(empresaPersistida);
//            e1.setCnpj("12345678000190"); // 14 dígitos → máscara termina com 0190
//
//            Empresa e2 = clone(empresaPersistida);
//            e2.setId(UUID.randomUUID());
//            e2.setEmail("outro@acme.com");
//            e2.setCnpj(null);             // força a máscara padrão "**************"
//
//            when(repository.findAll()).thenReturn(List.of(e1, e2));
//
//            List<EmpresaListDTO> lista = service.listar();
//
//            assertThat(lista).hasSize(2);
//            assertThat(lista.get(0).getCnpjMascarado()).endsWith("0190");
//            assertThat(lista.get(1).getCnpjMascarado()).isEqualTo("**************");
//        }

        private Empresa clone(Empresa e) {
            Empresa c = new Empresa();
            c.setId(e.getId());
            c.setCnpj(e.getCnpj());
            c.setRazaoSocial(e.getRazaoSocial());
            c.setNomeResponsavel(e.getNomeResponsavel());
            c.setEmail(e.getEmail());
            c.setSenhaHash(e.getSenhaHash());
            c.setTelefone(e.getTelefone());
            c.setStatus(e.getStatus());
            c.setDataCriacao(e.getDataCriacao());
            c.setDataAtualizacao(e.getDataAtualizacao());
            return c;
        }
    }
}
