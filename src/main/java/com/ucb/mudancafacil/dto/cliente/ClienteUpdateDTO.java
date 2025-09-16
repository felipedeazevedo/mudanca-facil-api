package com.ucb.mudancafacil.dto.cliente;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteUpdateDTO {
    @Size(max = 150)
    private String nome;

    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String telefone;

    @Valid
    private EnderecoDTO endereco;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EnderecoDTO {

        @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos (sem máscara)")
        private String cep;

        @Size(max = 120)
        private String logradouro;

        @Size(max = 10)
        private String numero;

        @Size(max = 60)
        private String complemento;

        @Size(max = 80)
        private String bairro;

        @Size(max = 120)
        private String cidade;

        @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter 2 letras maiúsculas (ex.: DF)")
        private String uf;
    }
}
