package com.kluska.contratacaofuncionariopicpay.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IndicadoresFuncionariosResponse {

    long totalCandidatos;
    long emAnalise;
    long aprovados;
    long reprovados;
    long contratados;
}
