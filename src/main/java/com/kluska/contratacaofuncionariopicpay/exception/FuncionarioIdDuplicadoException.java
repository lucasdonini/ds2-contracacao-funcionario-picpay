package com.kluska.contratacaofuncionariopicpay.exception;

public class FuncionarioIdDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FuncionarioIdDuplicadoException(Long id) {
        super("Já existe um funcionário cadastrado com o ID " + id + ".");
    }
}
