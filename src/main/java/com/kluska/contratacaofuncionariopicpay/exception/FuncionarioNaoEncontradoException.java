package com.kluska.contratacaofuncionariopicpay.exception;

public class FuncionarioNaoEncontradoException extends RuntimeException {

    public FuncionarioNaoEncontradoException(Long id) {
        super("Funcionário com ID " + id + " não foi encontrado.");
    }
}
