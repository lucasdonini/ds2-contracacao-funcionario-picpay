package com.kluska.contratacaofuncionariopicpay.config;

import com.kluska.contratacaofuncionariopicpay.controller.FuncionarioController;
import com.kluska.contratacaofuncionariopicpay.exception.RestExceptionHandler;
import com.kluska.contratacaofuncionariopicpay.repository.FuncionarioRepository;
import com.kluska.contratacaofuncionariopicpay.service.FuncionarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// MockMvc andExpect executa as asserções de resposta HTTP.
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
class CorsConfigTest {

    @ParameterizedTest
    @ValueSource(strings = {"http://localhost:5173", "https://host-aleatorio.com"})
    void devePermitirRequisicoesDeQualquerOrigem(String origem) throws Exception {
        try (AnnotationConfigWebApplicationContext context = criarContexto()) {
            MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

            mockMvc.perform(options("/funcionarios")
                            .header(HttpHeaders.ORIGIN, origem)
                            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "content-type"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(
                            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                            "*"
                    ))
                    .andExpect(header().string(
                            HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                            "GET,POST,PUT,PATCH,DELETE,OPTIONS"
                    ));
        }
    }

    private AnnotationConfigWebApplicationContext criarContexto() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(MvcConfiguration.class);
        context.refresh();
        return context;
    }

    @Configuration
    @EnableWebMvc
    @Import(CorsConfig.class)
    static class MvcConfiguration {

        @Bean
        FuncionarioRepository funcionarioRepository() {
            return new FuncionarioRepository();
        }

        @Bean
        FuncionarioService funcionarioService(FuncionarioRepository repository) {
            return new FuncionarioService(repository);
        }

        @Bean
        FuncionarioController funcionarioController(FuncionarioService service) {
            return new FuncionarioController(service);
        }

        @Bean
        RestExceptionHandler restExceptionHandler() {
            return new RestExceptionHandler();
        }
    }
}

