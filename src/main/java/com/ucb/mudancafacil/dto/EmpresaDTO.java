package com.ucb.mudancafacil.dto;

import com.ucb.mudancafacil.enums.PorteEmpresa;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class EmpresaDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private PorteEmpresa porte;
    private LocalTime horarioInicioDisponibilidade;
    private LocalTime horarioFimDisponibilidade;
    private String raAtuacao;
    private Double mediaPrecoMudancaPequena;
    private Double mediaPrecoMudancaMedia;
    private Double mediaPrecoMudancaGrande;
}