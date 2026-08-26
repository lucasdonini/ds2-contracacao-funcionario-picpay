package com.kluska.contratacaofuncionariopicpay.controller;

import com.kluska.contratacaofuncionariopicpay.exception.RestExceptionHandler;
import com.kluska.contratacaofuncionariopicpay.repository.FuncionarioRepository;
import com.kluska.contratacaofuncionariopicpay.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FuncionarioControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        FuncionarioService service = new FuncionarioService(new FuncionarioRepository());
        FuncionarioController controller = new FuncionarioController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void deveCadastrarFuncionario() throws Exception {
        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDeCadastro(1L)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/funcionarios/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Ana Silva"))
                .andExpect(jsonPath("$.status").value("EM_ANALISE"));
    }

    @Test
    void deveValidarCamposObrigatoriosNoCadastro() throws Exception {
        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "email": "email-invalido",
                                  "cargo": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Um ou mais campos enviados são inválidos."))
                .andExpect(jsonPath("$.campos.length()").value(3));
    }

    @Test
    void deveRecusarIdDuplicado() throws Exception {
        cadastrar(7L);

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDeCadastro(7L)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Já existe um funcionário cadastrado com o ID 7."));
    }

    @Test
    void deveConsultarTodosEConsultarPorId() throws Exception {
        cadastrar(3L);

        mockMvc.perform(get("/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3));

        mockMvc.perform(get("/funcionarios/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@example.com"));
    }

    @Test
    void deveRetornarNaoEncontradoAoConsultarIdInexistente() throws Exception {
        mockMvc.perform(get("/funcionarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Funcionário com ID 999 não foi encontrado."));
    }

    @Test
    void deveAtualizarCompletamenteComPut() throws Exception {
        cadastrar(4L);

        mockMvc.perform(put("/funcionarios/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Bruno Souza",
                                  "email": "bruno@example.com",
                                  "telefone": "11988887777",
                                  "cargo": "Tech Lead",
                                  "departamento": "Tecnologia",
                                  "salario": 12000.00,
                                  "cidade": "Campinas",
                                  "status": "APROVADO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nome").value("Bruno Souza"))
                .andExpect(jsonPath("$.status").value("APROVADO"));
    }

    @Test
    void deveAtualizarParcialmenteComPatch() throws Exception {
        cadastrar(5L);

        mockMvc.perform(patch("/funcionarios/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cargo": "Especialista",
                                  "status": "CONTRATADO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Silva"))
                .andExpect(jsonPath("$.cargo").value("Especialista"))
                .andExpect(jsonPath("$.status").value("CONTRATADO"));
    }

    @Test
    void deveRecusarPatchVazio() throws Exception {
        cadastrar(6L);

        mockMvc.perform(patch("/funcionarios/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(
                        "Informe ao menos um dos campos permitidos no PATCH: cargo, status ou salário."
                ));
    }

    @Test
    void deveExcluirFuncionario() throws Exception {
        cadastrar(8L);

        mockMvc.perform(delete("/funcionarios/8"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/funcionarios/8"))
                .andExpect(status().isNotFound());
    }

    private void cadastrar(Long id) throws Exception {
        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDeCadastro(id)))
                .andExpect(status().isCreated());
    }

    private String jsonDeCadastro(Long id) {
        return """
                {
                  "id": %d,
                  "nome": "Ana Silva",
                  "email": "ana@example.com",
                  "telefone": "11999999999",
                  "cargo": "Desenvolvedora",
                  "departamento": "Tecnologia",
                  "salario": 8500.00,
                  "cidade": "São Paulo"
                }
                """.formatted(id);
    }
}
