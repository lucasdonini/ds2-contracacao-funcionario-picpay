package com.kluska.contratacaofuncionariopicpay.dto;

import com.kluska.contratacaofuncionariopicpay.domain.StatusFuncionario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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
public class AtualizarParcialmenteFuncionarioRequest {

    @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres.")
    private String cargo;

    private StatusFuncionario status;

    @DecimalMin(value = "0.0", inclusive = true, message = "O salário não pode ser negativo.")
    @Digits(integer = 12, fraction = 2, message = "O salário deve ter no máximo 12 inteiros e 2 casas decimais.")
    private BigDecimal salario;
}
