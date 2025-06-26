package com.ucb.mudancafacil.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    private LocalTime horarioInicioDisponibilidade;
    private LocalTime horarioFimDisponibilidade;

    private String raAtuacao;

    private Double mediaPrecoMudancaPequena;
    private Double mediaPrecoMudancaMedia;
    private Double mediaPrecoMudancaGrande;
}
