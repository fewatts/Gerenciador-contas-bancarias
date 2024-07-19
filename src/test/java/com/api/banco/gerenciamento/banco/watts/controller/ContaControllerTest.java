package com.api.banco.gerenciamento.banco.watts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosDetalhamentoConta;
import com.api.banco.gerenciamento.banco.watts.domain.model.Conta;
import com.api.banco.gerenciamento.banco.watts.domain.model.TipoDeConta;
import com.api.banco.gerenciamento.banco.watts.domain.repository.ContaRepository;
import com.api.banco.gerenciamento.banco.watts.domain.service.ContaService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ContaControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private ContaService contaService;

        @MockBean
        private ContaRepository contaRepository;

        @Autowired
        private JacksonTester<DadosCadastroContaBancaria> dadosCadastroContaBancariaJt;

        @Autowired
        private JacksonTester<DadosDetalhamentoConta> dadosDetalhamentoContaJt;

        @Test
        @DisplayName("POST criar contas com dados válidos deve retornar 201")
        void criarContaCenarioUm() throws Exception {
                var dados = new DadosCadastroContaBancaria("001", TipoDeConta.CORRENTE, "Fulado cliente",
                                new BigDecimal("1723.23"), true);

                var contaCriada = new Conta(null, "001", TipoDeConta.CORRENTE, "Fulado cliente",
                                new BigDecimal("1723.23"), true);

                when(contaService.criarConta(any(DadosCadastroContaBancaria.class))).thenReturn(contaCriada);

                var response = mvc.perform(
                                post("/contas")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(dadosCadastroContaBancariaJt.write(dados).getJson()))
                                .andReturn().getResponse();

                var jsonEsperado = dadosDetalhamentoContaJt.write(
                                new DadosDetalhamentoConta(null, "001", TipoDeConta.CORRENTE,
                                                "Fulado cliente", new BigDecimal("1723.23"), true))
                                .getJson();

                assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
                assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        }

}
