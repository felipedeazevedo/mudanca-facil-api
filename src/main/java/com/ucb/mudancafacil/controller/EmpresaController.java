package com.ucb.mudancafacil.controller;

import com.ucb.mudancafacil.dto.EmpresaCreateDTO;
import com.ucb.mudancafacil.dto.EmpresaListDTO;
import com.ucb.mudancafacil.dto.EmpresaUpdateDTO;
import com.ucb.mudancafacil.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @GetMapping
    public List<EmpresaListDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaListDTO> buscarPorId(@PathVariable UUID id) {
        EmpresaListDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EmpresaListDTO> criar(@Valid @RequestBody EmpresaCreateDTO dto) {
        EmpresaListDTO criada = service.criar(dto);
        return ResponseEntity
                .created(URI.create("/empresas/" + criada.getId()))
                .body(criada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmpresaListDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EmpresaUpdateDTO dto) {
        EmpresaListDTO updated = service.patch(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.ok("Empresa " + id + " excluída com sucesso.");
    }

    @GetMapping("/me")
    public EmpresaListDTO me(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {// ID da empresa
        String email = jwt.getClaim("email");
        return service.buscarPorEmail(email);
    }
}
