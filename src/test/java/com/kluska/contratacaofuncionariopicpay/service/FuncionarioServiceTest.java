package com.kluska.contratacaofuncionariopicpay.service;

import com.kluska.contratacaofuncionariopicpay.domain.StatusFuncionario;
import com.kluska.contratacaofuncionariopicpay.dto.AtualizarFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.AtualizarParcialmenteFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.CriarFuncionarioRequest;
import com.kluska.contratacaofuncionariopicpay.dto.FuncionarioResponse;
import com.kluska.contratacaofuncionariopicpay.exception.FuncionarioNaoEncontradoException;
import com.kluska.contratacaofuncionariopicpay.exception.RequisicaoInvalidaException;
import com.kluska.contratacaofuncionariopicpay.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FuncionarioServiceTest {

    private FuncionarioService service;

    @BeforeEach
    void configurar() {
        service = new FuncionarioService(new FuncionarioRepository());
    }

    @Test
    void deveCadastrarComStatusEmAnalisePorPadrao() {
        FuncionarioResponse funcionario = service.cadastrar(novaRequisicao());

        assertThat(funcionario.getId()).isPositive();
        assertThat(funcionario.getStatus()).isEqualTo(StatusFuncionario.EM_ANALISE);
        assertThat(funcionario.getNome()).isEqualTo("Ana Silva");
    }

    @Test
    void deveSubstituirTodosOsDadosEditaveisNoPut() {
        FuncionarioResponse cadastrado = service.cadastrar(novaRequisicao());
        AtualizarFuncionarioRequest atualizacao = AtualizarFuncionarioRequest.builder()
                .nome("Bruno Souza")
                .email("bruno@example.com")
                .telefone("11988887777")
                .cargo("Tech Lead")
                .departamento("Tecnologia")
                .salario(new BigDecimal("12500.00"))
                .cidade("Campinas")
                .status(StatusFuncionario.APROVADO)
                .build();

        FuncionarioResponse atualizado = service.atualizarCompletamente(cadastrado.getId(), atualizacao);

        assertThat(atualizado.getId()).isEqualTo(cadastrado.getId());
        assertThat(atualizado.getNome()).isEqualTo("Bruno Souza");
        assertThat(atualizado.getCargo()).isEqualTo("Tech Lead");
        assertThat(atualizado.getStatus()).isEqualTo(StatusFuncionario.APROVADO);
    }

    @Test
    void deveAlterarSomenteCamposInformadosNoPatch() {
        FuncionarioResponse cadastrado = service.cadastrar(novaRequisicao());
        AtualizarParcialmenteFuncionarioRequest patch = AtualizarParcialmenteFuncionarioRequest.builder()
                .status(StatusFuncionario.CONTRATADO)
                .salario(new BigDecimal("9000.00"))
                .build();

        FuncionarioResponse atualizado = service.atualizarParcialmente(cadastrado.getId(), patch);

        assertThat(atualizado.getNome()).isEqualTo(cadastrado.getNome());
        assertThat(atualizado.getCargo()).isEqualTo(cadastrado.getCargo());
        assertThat(atualizado.getStatus()).isEqualTo(StatusFuncionario.CONTRATADO);
        assertThat(atualizado.getSalario()).isEqualByComparingTo("9000.00");
    }

    @Test
    void deveRecusarPatchSemCamposPermitidos() {
        FuncionarioResponse cadastrado = service.cadastrar(novaRequisicao());

        assertThatThrownBy(() -> service.atualizarParcialmente(
                cadastrado.getId(),
                new AtualizarParcialmenteFuncionarioRequest()
        ))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessageContaining("ao menos um");
    }

    @Test
    void deveInformarQuandoFuncionarioNaoExiste() {
        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(FuncionarioNaoEncontradoException.class)
                .hasMessageContaining("999");
    }

    private CriarFuncionarioRequest novaRequisicao() {
        return CriarFuncionarioRequest.builder()
                .nome("  Ana Silva  ")
                .email("ana@example.com")
                .telefone("11999999999")
                .cargo("Desenvolvedora")
                .departamento("Tecnologia")
                .salario(new BigDecimal("8500.00"))
                .cidade("São Paulo")
                .build();
    }
}
