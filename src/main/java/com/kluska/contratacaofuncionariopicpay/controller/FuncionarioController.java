package com.kluska.contratacaofuncionariopicpay.controller;

import com.kluska.contratacaofuncionariopicpay.dto.AtualizarFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.AtualizarParcialmenteFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.CriarFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.FuncionarioResponse;
import com.kluska.contratacaofuncionariopicpay.service.FuncionarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioResponse> cadastrar(
            @Valid @RequestBody CriarFuncionarioRequest request
    ) {
        FuncionarioResponse funcionario = funcionarioService.cadastrar(request);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(funcionario.getId())
                .toUri();

        return ResponseEntity.created(localizacao).body(funcionario);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponse>> listarTodos() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(
            @PathVariable @Positive(message = "O ID deve ser um número positivo.") Long id
    ) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizarCompletamente(
            @PathVariable @Positive(message = "O ID deve ser um número positivo.") Long id,
            @Valid @RequestBody AtualizarFuncionarioRequest request
    ) {
        return ResponseEntity.ok(funcionarioService.atualizarCompletamente(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizarParcialmente(
            @PathVariable @Positive(message = "O ID deve ser um número positivo.") Long id,
            @Valid @RequestBody AtualizarParcialmenteFuncionarioRequest request
    ) {
        return ResponseEntity.ok(funcionarioService.atualizarParcialmente(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable @Positive(message = "O ID deve ser um número positivo.") Long id
    ) {
        funcionarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
