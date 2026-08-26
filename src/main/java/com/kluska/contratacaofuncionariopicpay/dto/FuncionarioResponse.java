package com.kluska.contratacaofuncionariopicpay.dto;

import com.kluska.contratacaofuncionariopicpay.domain.StatusFuncionario;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class FuncionarioResponse {

    Long id;
    String nome;
    String email;
    String telefone;
    String cargo;
    String departamento;
    BigDecimal salario;
    String cidade;
    StatusFuncionario status;
}
