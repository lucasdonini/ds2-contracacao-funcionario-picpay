package com.kluska.contratacaofuncionariopicpay.repository;

import com.kluska.contratacaofuncionariopicpay.domain.Funcionario;
import com.kluska.contratacaofuncionariopicpay.exception.FuncionarioIdDuplicadoException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FuncionarioRepository {

    private final ArrayList<Funcionario> funcionarios = new ArrayList<>();
    private long proximoId = 1L;

    public synchronized Funcionario inserir(Funcionario funcionario) {
        Funcionario novoFuncionario = copiar(funcionario);

        if (novoFuncionario.getId() == null) {
            novoFuncionario.setId(gerarId());
        } else {
            validarIdUnico(novoFuncionario.getId());
            ajustarProximoId(novoFuncionario.getId());
        }

        funcionarios.add(novoFuncionario);
        return copiar(novoFuncionario);
    }

    public synchronized List<Funcionario> buscarTodos() {
        return funcionarios.stream()
                .map(this::copiar)
                .toList();
    }

    public synchronized Optional<Funcionario> buscarPorId(Long id) {
        return funcionarios.stream()
                .filter(funcionario -> funcionario.getId().equals(id))
                .findFirst()
                .map(this::copiar);
    }

    public synchronized Optional<Funcionario> atualizar(Funcionario funcionario) {
        for (int indice = 0; indice < funcionarios.size(); indice++) {
            if (funcionarios.get(indice).getId().equals(funcionario.getId())) {
                Funcionario funcionarioAtualizado = copiar(funcionario);
                funcionarios.set(indice, funcionarioAtualizado);
                return Optional.of(copiar(funcionarioAtualizado));
            }
        }

        return Optional.empty();
    }

    public synchronized boolean removerPorId(Long id) {
        return funcionarios.removeIf(funcionario -> funcionario.getId().equals(id));
    }

    private long gerarId() {
        while (idJaExiste(proximoId)) {
            incrementarProximoId();
        }

        long idGerado = proximoId;
        incrementarProximoId();
        return idGerado;
    }

    private void validarIdUnico(Long id) {
        if (idJaExiste(id)) {
            throw new FuncionarioIdDuplicadoException(id);
        }
    }

    private boolean idJaExiste(Long id) {
        return funcionarios.stream()
                .anyMatch(funcionario -> funcionario.getId().equals(id));
    }

    private void ajustarProximoId(long idInformado) {
        if (idInformado >= proximoId) {
            proximoId = idInformado == Long.MAX_VALUE ? 1L : idInformado + 1L;
        }
    }

    private void incrementarProximoId() {
        proximoId = proximoId == Long.MAX_VALUE ? 1L : proximoId + 1L;
    }

    private Funcionario copiar(Funcionario funcionario) {
        return funcionario.toBuilder().build();
    }
}
