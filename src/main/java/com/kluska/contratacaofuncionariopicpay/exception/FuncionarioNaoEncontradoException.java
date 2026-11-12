package com.kluska.contratacaofuncionariopicpay.exception;

public class FuncionarioNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FuncionarioNaoEncontradoException(Long id) {
        super("Funcionário com ID " + id + " não foi encontrado.");
    }
}
