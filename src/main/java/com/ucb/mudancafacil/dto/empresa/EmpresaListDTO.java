package com.ucb.mudancafacil.dto.empresa;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class EmpresaListDTO {
    private UUID id;
    private String cnpjMascarado;
    private String razaoSocial;
    private String nomeResponsavel;
    private String email;
    private String telefone;
    private String status;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
}
