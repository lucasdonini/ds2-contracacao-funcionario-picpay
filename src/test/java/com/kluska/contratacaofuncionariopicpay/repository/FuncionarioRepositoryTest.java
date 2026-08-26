package com.kluska.contratacaofuncionariopicpay.repository;

import com.kluska.contratacaofuncionariopicpay.domain.Funcionario;
import com.kluska.contratacaofuncionariopicpay.domain.StatusFuncionario;
import com.kluska.contratacaofuncionariopicpay.exception.FuncionarioIdDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FuncionarioRepositoryTest {

    private FuncionarioRepository repository;

    @BeforeEach
    void configurar() {
        repository = new FuncionarioRepository();
    }

    @Test
    void deveGerarIdsSequenciaisQuandoIdNaoForInformado() {
        Funcionario primeiro = repository.inserir(novoFuncionario(null, "Ana"));
        Funcionario segundo = repository.inserir(novoFuncionario(null, "Bruno"));

        assertThat(primeiro.getId()).isEqualTo(1L);
        assertThat(segundo.getId()).isEqualTo(2L);
    }

    @Test
    void deveRespeitarIdInformadoEAjustarProximaSequencia() {
        repository.inserir(novoFuncionario(10L, "Ana"));

        Funcionario gerado = repository.inserir(novoFuncionario(null, "Bruno"));

        assertThat(gerado.getId()).isEqualTo(11L);
    }

    @Test
    void deveRecusarIdDuplicado() {
        repository.inserir(novoFuncionario(5L, "Ana"));

        assertThatThrownBy(() -> repository.inserir(novoFuncionario(5L, "Bruno")))
                .isInstanceOf(FuncionarioIdDuplicadoException.class)
                .hasMessageContaining("ID 5");
    }

    @Test
    void deveProtegerObjetosArmazenadosContraAlteracoesExternas() {
        Funcionario salvo = repository.inserir(novoFuncionario(null, "Ana"));
        salvo.setNome("Nome alterado fora do repositório");

        Funcionario recuperado = repository.buscarPorId(salvo.getId()).orElseThrow();

        assertThat(recuperado.getNome()).isEqualTo("Ana");
    }

    private Funcionario novoFuncionario(Long id, String nome) {
        return Funcionario.builder()
                .id(id)
                .nome(nome)
                .email(nome.toLowerCase() + "@example.com")
                .cargo("Desenvolvedor")
                .status(StatusFuncionario.EM_ANALISE)
                .build();
    }
}
