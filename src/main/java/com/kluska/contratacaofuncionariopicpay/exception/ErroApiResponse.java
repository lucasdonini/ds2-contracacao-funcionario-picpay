package com.kluska.contratacaofuncionariopicpay.exception;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class ErroApiResponse {

    Instant timestamp;
    int status;
    String erro;
    String mensagem;
    String caminho;
    List<CampoInvalidoResponse> campos;
}
