package com.ucb.mudancafacil.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmpresaUpdateDTO {

    // Se permitir trocar a razão social
    @Size(max = 255)
    private String razaoSocial;

    @Size(max = 150)
    private String nomeResponsavel;

    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String telefone;

    // Opcional: permitir alterar status via admin/fluxo específico
    // Valores: PENDING | READY_FOR_LEADS | ACTIVE | INACTIVE
    private String status;

    // Observação: CNPJ e senha NÃO devem ser alterados por este PATCH.
    // - CNPJ normalmente é imutável (chave legal/fiscal)
    // - Senha deve ter endpoint próprio (troca com senha atual, 2FA, etc.)
}
