package com.kluska.contratacaofuncionariopicpay.dto;

import com.kluska.contratacaofuncionariopicpay.domain.StatusFuncionario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarFuncionarioRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ter um formato válido.")
    @Size(max = 160, message = "O e-mail deve ter no máximo 160 caracteres.")
    private String email;

    @Size(max = 30, message = "O telefone deve ter no máximo 30 caracteres.")
    private String telefone;

    @NotBlank(message = "O cargo é obrigatório.")
    @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres.")
    private String cargo;

    @Size(max = 100, message = "O departamento deve ter no máximo 100 caracteres.")
    private String departamento;

    @DecimalMin(value = "0.0", inclusive = true, message = "O salário não pode ser negativo.")
    @Digits(integer = 12, fraction = 2, message = "O salário deve ter no máximo 12 inteiros e 2 casas decimais.")
    private BigDecimal salario;

    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
    private String cidade;

    @NotNull(message = "O status é obrigatório na atualização completa.")
    private StatusFuncionario status;
}
