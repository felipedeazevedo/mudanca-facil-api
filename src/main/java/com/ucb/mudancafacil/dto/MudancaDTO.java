package com.ucb.mudancafacil.dto;

import com.ucb.mudancafacil.enums.TipoMudanca;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MudancaDTO {
    private Long id;
    private String origem;
    private String destino;
    private LocalDateTime dataHoraMudanca;
    private TipoMudanca tipoMudanca;
    private String categoria;
    private Long clienteId;
}