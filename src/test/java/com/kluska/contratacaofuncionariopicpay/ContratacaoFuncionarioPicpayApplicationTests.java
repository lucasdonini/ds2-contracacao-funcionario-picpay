package com.kluska.contratacaofuncionariopicpay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContratacaoFuncionarioPicpayApplicationTests {

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.context.ApplicationContext context;

    @Test
    void contextLoads() {
        org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(ContratacaoFuncionarioPicpayApplication.class), "O contexto deve carregar a aplicacao");
    }

}
