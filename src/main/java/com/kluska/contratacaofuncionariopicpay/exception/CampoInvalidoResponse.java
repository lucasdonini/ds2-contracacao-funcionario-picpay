package com.kluska.contratacaofuncionariopicpay.exception;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CampoInvalidoResponse {

    String campo;
    String mensagem;
}
