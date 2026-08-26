package com.kluska.contratacaofuncionariopicpay.service;

import com.kluska.contratacaofuncionariopicpay.domain.Funcionario;
import com.kluska.contratacaofuncionariopicpay.domain.StatusFuncionario;
import com.kluska.contratacaofuncionariopicpay.dto.AtualizarFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.AtualizarParcialmenteFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.CriarFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.FuncionarioResponse;
import com.kluska.contratacaofuncionariopicpay.exception.FuncionarioNaoEncontradoException;
import com.kluska.contratacaofuncionariopicpay.exception.RequisicaoInvalidaException;
import com.kluska.contratacaofuncionariopicpay.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioResponse cadastrar(CriarFuncionarioRequest request) {
        Funcionario funcionario = Funcionario.builder()
                .id(request.getId())
                .nome(normalizar(request.getNome()))
                .email(normalizar(request.getEmail()))
                .telefone(normalizarOpcional(request.getTelefone()))
                .cargo(normalizar(request.getCargo()))
                .departamento(normalizarOpcional(request.getDepartamento()))
                .salario(request.getSalario())
                .cidade(normalizarOpcional(request.getCidade()))
                .status(request.getStatus() == null ? StatusFuncionario.EM_ANALISE : request.getStatus())
                .build();

        return paraResponse(funcionarioRepository.inserir(funcionario));
    }

    public List<FuncionarioResponse> listarTodos() {
        return funcionarioRepository.buscarTodos().stream()
                .map(this::paraResponse)
                .toList();
    }

    public FuncionarioResponse buscarPorId(Long id) {
        return paraResponse(buscarFuncionario(id));
    }

    public FuncionarioResponse atualizarCompletamente(Long id, AtualizarFuncionarioRequest request) {
        buscarFuncionario(id);

        Funcionario funcionarioAtualizado = Funcionario.builder()
                .id(id)
                .nome(normalizar(request.getNome()))
                .email(normalizar(request.getEmail()))
                .telefone(normalizarOpcional(request.getTelefone()))
                .cargo(normalizar(request.getCargo()))
                .departamento(normalizarOpcional(request.getDepartamento()))
                .salario(request.getSalario())
                .cidade(normalizarOpcional(request.getCidade()))
                .status(request.getStatus())
                .build();

        return funcionarioRepository.atualizar(funcionarioAtualizado)
                .map(this::paraResponse)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
    }

    public FuncionarioResponse atualizarParcialmente(
            Long id,
            AtualizarParcialmenteFuncionarioRequest request
    ) {
        validarPatch(request);
        Funcionario funcionario = buscarFuncionario(id);

        if (request.getCargo() != null) {
            funcionario.setCargo(normalizar(request.getCargo()));
        }
        if (request.getStatus() != null) {
            funcionario.setStatus(request.getStatus());
        }
        if (request.getSalario() != null) {
            funcionario.setSalario(request.getSalario());
        }

        return funcionarioRepository.atualizar(funcionario)
                .map(this::paraResponse)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
    }

    public void excluir(Long id) {
        if (!funcionarioRepository.removerPorId(id)) {
            throw new FuncionarioNaoEncontradoException(id);
        }
    }

    private Funcionario buscarFuncionario(Long id) {
        return funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
    }

    private void validarPatch(AtualizarParcialmenteFuncionarioRequest request) {
        if (request.getCargo() == null && request.getStatus() == null && request.getSalario() == null) {
            throw new RequisicaoInvalidaException(
                    "Informe ao menos um dos campos permitidos no PATCH: cargo, status ou salário."
            );
        }

        if (request.getCargo() != null && request.getCargo().isBlank()) {
            throw new RequisicaoInvalidaException("O cargo não pode ser vazio.");
        }
    }

    private String normalizar(String valor) {
        return valor.trim();
    }

    private String normalizarOpcional(String valor) {
        return valor == null ? null : valor.trim();
    }

    private FuncionarioResponse paraResponse(Funcionario funcionario) {
        return FuncionarioResponse.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .email(funcionario.getEmail())
                .telefone(funcionario.getTelefone())
                .cargo(funcionario.getCargo())
                .departamento(funcionario.getDepartamento())
                .salario(funcionario.getSalario())
                .cidade(funcionario.getCidade())
                .status(funcionario.getStatus())
                .build();
    }
}
