package com.kluska.contratacaofuncionariopicpay.exception;

public class FuncionarioIdDuplicadoException extends RuntimeException {

    public FuncionarioIdDuplicadoException(Long id) {
        super("Já existe um funcionário cadastrado com o ID " + id + ".");
    }
}
