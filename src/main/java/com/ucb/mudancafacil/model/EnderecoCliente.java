package com.ucb.mudancafacil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class EnderecoCliente {

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos (sem máscara)")
    @Column(name = "endereco_cep", length = 8, nullable = false)
    private String cep;

    @NotBlank
    @Size(max = 120)
    @Column(name = "endereco_logradouro", length = 120, nullable = false)
    private String logradouro;

    @NotBlank
    @Size(max = 10)
    @Column(name = "endereco_numero", length = 10, nullable = false)
    private String numero;

    @Size(max = 60)
    @Column(name = "endereco_complemento", length = 60)
    private String complemento;

    @NotBlank
    @Size(max = 80)
    @Column(name = "endereco_bairro", length = 80, nullable = false)
    private String bairro;

    @NotBlank
    @Size(max = 120)
    @Column(name = "endereco_cidade", length = 120, nullable = false)
    private String cidade;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter 2 letras maiúsculas (ex.: DF)")
    @Column(name = "endereco_uf", length = 2, nullable = false)
    private String uf;
}
