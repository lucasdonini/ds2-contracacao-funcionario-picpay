package com.kluska.contratacaofuncionariopicpay.config;

import com.kluska.contratacaofuncionariopicpay.controller.FuncionarioController;
import com.kluska.contratacaofuncionariopicpay.exception.RestExceptionHandler;
import com.kluska.contratacaofuncionariopicpay.repository.FuncionarioRepository;
import com.kluska.contratacaofuncionariopicpay.service.FuncionarioService;
import org.junit.jupiter.api.Test;
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

class CorsConfigTest {

    @Test
    void devePermitirRequisicoesDoFrontendLocal() throws Exception {
        try (AnnotationConfigWebApplicationContext context = criarContexto()) {
            MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

            mockMvc.perform(options("/funcionarios")
                            .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "content-type"))
                    .andExpect(status().isOk())
                    .andExpect(header().string(
                            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                            "http://localhost:5173"
                    ))
                    .andExpect(header().string(
                            HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                            "GET,POST,PUT,PATCH,DELETE,OPTIONS"
                    ));
        }
    }

    @Test
    void deveRecusarOrigemNaoAutorizada() throws Exception {
        try (AnnotationConfigWebApplicationContext context = criarContexto()) {
            MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

            mockMvc.perform(options("/funcionarios")
                            .header(HttpHeaders.ORIGIN, "https://origem-nao-autorizada.example")
                            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                    .andExpect(status().isForbidden());
        }
    }

    private AnnotationConfigWebApplicationContext criarContexto() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(TestConfiguration.class);
        context.refresh();
        return context;
    }

    @Configuration
    @EnableWebMvc
    @Import(CorsConfig.class)
    static class TestConfiguration {

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

