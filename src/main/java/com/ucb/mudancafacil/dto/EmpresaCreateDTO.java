package com.ucb.mudancafacil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaCreateDTO {

    @NotBlank
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter exatamente 14 dígitos")
    private String cnpj;

    @NotBlank
    @Size(max = 255)
    private String razaoSocial;

    @NotBlank
    @Size(max = 150)
    private String nomeResponsavel;

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
}
