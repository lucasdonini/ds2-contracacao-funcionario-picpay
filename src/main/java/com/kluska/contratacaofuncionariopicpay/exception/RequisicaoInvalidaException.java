package com.kluska.contratacaofuncionariopicpay.exception;

public class RequisicaoInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RequisicaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
