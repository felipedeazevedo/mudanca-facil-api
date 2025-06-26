package com.ucb.mudancafacil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String tipoUsuario;
    private Long id;
    private String nome;

    public LoginResponse(String tipoUsuario, Long id, String nome) {
        this.tipoUsuario = tipoUsuario;
        this.id = id;
        this.nome = nome;
    }
}
