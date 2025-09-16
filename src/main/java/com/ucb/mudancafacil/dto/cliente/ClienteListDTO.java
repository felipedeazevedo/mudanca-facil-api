package com.ucb.mudancafacil.dto.cliente;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ClienteListDTO {
    private UUID id;
    private String cpfMascarado;
    private String nome;
    private String email;
    private String telefone;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;

    private EnderecoDTO endereco;

    @Getter
    @Setter
    public static class EnderecoDTO {
        private String cep;
        private String logradouro;
        private String numero;
        private String complemento;
        private String bairro;
        private String cidade;
        private String uf;
    }
}
