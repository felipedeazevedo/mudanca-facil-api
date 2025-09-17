package com.ucb.mudancafacil.controller;

import com.ucb.mudancafacil.dto.empresa.EmpresaCreateDTO;
import com.ucb.mudancafacil.dto.empresa.EmpresaListDTO;
import com.ucb.mudancafacil.dto.empresa.EmpresaUpdateDTO;
import com.ucb.mudancafacil.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @GetMapping
    public ResponseEntity<Page<EmpresaListDTO>> listar(
            @PageableDefault(size = 20, sort = "razaoSocial") Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public ResponseEntity<EmpresaListDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EmpresaListDTO> criar(@Valid @RequestBody EmpresaCreateDTO dto) {
        EmpresaListDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PatchMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public ResponseEntity<EmpresaListDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EmpresaUpdateDTO dto) {
        return ResponseEntity.ok(service.patch(id, dto));
    }

    @DeleteMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/me")
    public ResponseEntity<EmpresaListDTO> me(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null)
            return ResponseEntity.status(401).build();

        String sub = jwt.getSubject();
        UUID id = null;
        if (sub != null) {
            try {
                id = UUID.fromString(sub);
            } catch (IllegalArgumentException ignored) { /* não era UUID */ }
        }

        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
