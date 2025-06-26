package com.ucb.mudancafacil.controller;

import com.ucb.mudancafacil.dto.MudancaDTO;
import com.ucb.mudancafacil.service.MudancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mudancas")
@RequiredArgsConstructor
public class MudancaController {

    private final MudancaService service;

    @GetMapping
    public List<MudancaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MudancaDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MudancaDTO> criar(@RequestBody MudancaDTO dto) {
        MudancaDTO criado = service.salvar(dto);
        return ResponseEntity.ok(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MudancaDTO> atualizar(@PathVariable Long id, @RequestBody MudancaDTO dto) {
        return service.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MudancaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<MudancaDTO> mudancas = service.listarPorClienteId(clienteId);
        return ResponseEntity.ok(mudancas);
    }
}
