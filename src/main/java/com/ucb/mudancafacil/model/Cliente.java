package com.ucb.mudancafacil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(
        name = "cliente",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_cliente_cpf", columnNames = "cpf"),
                @UniqueConstraint(name = "uq_cliente_email", columnNames = "email")
        }
)
public class Cliente {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @NotBlank
    @Size(max = 20)
    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private OffsetDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private OffsetDateTime dataAtualizacao;

    @Valid
    @Embedded
    private EnderecoCliente endereco;
}
