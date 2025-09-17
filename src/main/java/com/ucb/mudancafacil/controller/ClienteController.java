package com.ucb.mudancafacil.controller;

import com.ucb.mudancafacil.dto.cliente.ClienteCreateDTO;
import com.ucb.mudancafacil.dto.cliente.ClienteListDTO;
import com.ucb.mudancafacil.dto.cliente.ClienteUpdateDTO;
import com.ucb.mudancafacil.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteListDTO> criar(@Valid @RequestBody ClienteCreateDTO dto) {
        ClienteListDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public ResponseEntity<ClienteListDTO> buscarPorId(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (!isSelf(jwt, id)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public ResponseEntity<ClienteListDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteUpdateDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (!isSelf(jwt, id)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.patch(id, dto));
    }

    @DeleteMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (!isSelf(jwt, id)) return ResponseEntity.status(403).build();
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ClienteListDTO> me(@AuthenticationPrincipal Jwt jwt) {
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

    private boolean isSelf(Jwt jwt, UUID resourceId) {
        if (jwt == null) return false;
        String sub = jwt.getSubject();
        return sub != null && sub.equalsIgnoreCase(resourceId.toString());
    }
}
