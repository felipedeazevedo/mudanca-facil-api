package com.ucb.mudancafacil.model;

import com.ucb.mudancafacil.enums.StatusEmpresa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(
        name = "empresa",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_empresa_cnpj", columnNames = "cnpj"),
                @UniqueConstraint(name = "uq_empresa_email", columnNames = "email")
        }
)
public class Empresa {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotBlank
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter exatamente 14 dígitos")
    @Column(name = "cnpj", length = 14, nullable = false)
    private String cnpj;

    @NotBlank
    @Size(max = 255)
    @Column(name = "razao_social", length = 255, nullable = false)
    private String razaoSocial;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nome_responsavel", length = 150, nullable = false)
    private String nomeResponsavel;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "senha_hash", length = 255, nullable = false)
    private String senhaHash;

    @NotBlank
    @Size(max = 20)
    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEmpresa status;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private OffsetDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private OffsetDateTime dataAtualizacao;

    @PrePersist
    public void prePersist() {
        if (status == null) status = StatusEmpresa.PENDING;
    }
}