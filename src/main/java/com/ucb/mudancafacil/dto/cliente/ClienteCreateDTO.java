package com.ucb.mudancafacil.dto.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCreateDTO {

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String senha;

    @NotBlank
    @Size(max = 20)
    private String telefone;

    @Valid
    @NotNull
    private EnderecoDTO endereco;

    @Getter
    @Setter
    public static class EnderecoDTO {
        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos (sem máscara)")
        private String cep;

        @NotBlank
        @Size(max = 120)
        private String logradouro;

        @NotBlank
        @Size(max = 10)
        private String numero;

        @Size(max = 60)
        private String complemento;

        @NotBlank
        @Size(max = 80)
        private String bairro;

        @NotBlank
        @Size(max = 120)
        private String cidade;

        @NotBlank
        @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter 2 letras maiúsculas (ex.: DF)")
        private String uf;
    }
}
