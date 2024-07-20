package com.api.banco.gerenciamento.banco.watts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosDetalhamentoConta;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosTransacaoTransferencia;
import com.api.banco.gerenciamento.banco.watts.domain.model.Conta;
import com.api.banco.gerenciamento.banco.watts.domain.model.TipoDeConta;
import com.api.banco.gerenciamento.banco.watts.domain.repository.ContaRepository;
import com.api.banco.gerenciamento.banco.watts.domain.service.ContaService;
import com.api.banco.gerenciamento.banco.watts.domain.validation.ValidacaoException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        @Autowired
        private JacksonTester<DadosTransacaoTransferencia> dadosTransacaoTransferenciaJt;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @DisplayName("POST criar contas com dados válidos deve retornar 201")
        void criarContaCenarioUm() throws Exception {
                var dados = criarDadosCadastroContaBancaria("001", TipoDeConta.CORRENTE, "Fulado cliente", "1723.23",
                                true);
                var contaCriada = criarConta(null, "001", TipoDeConta.CORRENTE, "Fulado cliente", "1723.23", true);

                when(contaService.criarConta(any(DadosCadastroContaBancaria.class))).thenReturn(contaCriada);

                var response = realizarPost("/contas", dadosCadastroContaBancariaJt.write(dados).getJson());

                var jsonEsperado = dadosDetalhamentoContaJt.write(new DadosDetalhamentoConta(null, "001",
                                TipoDeConta.CORRENTE, "Fulado cliente", new BigDecimal("1723.23"), true)).getJson();

                assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
                assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        @DisplayName("POST criar contas com dados inválidos deve retornar 400")
        void criarContaCenarioDois() throws Exception {
                var dados = criarDadosCadastroContaBancaria("002", TipoDeConta.CORRENTE, "Fulado cliente", "1723.23",
                                true);

                when(contaService.criarConta(any())).thenThrow(new ValidacaoException(
                                "Nosso Banco possui apenas a agência '001', Verifique sua entrada."));

                var response = realizarPost("/contas", dadosCadastroContaBancariaJt.write(dados).getJson());

                assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.getContentAsString())
                                .contains("Nosso Banco possui apenas a agência '001', Verifique sua entrada.");
        }

        @Test
        @DisplayName("POST de conta sem corpo na requisição deveria retornar 400")
        void criarContaCenarioTres() throws Exception {
                var response = mvc.perform(post("/contas")).andReturn().getResponse();

                assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }

        @Test
        @DisplayName("POST de transferencia valida deve retornar OK")
        void transferirEntreContasCenarioUm() throws IOException, Exception {
                var dados = criarDadosTransacaoTransferencia("1000.00");

                var contaOrigem = criarConta(null, "001", TipoDeConta.CORRENTE, "Fulado cliente", "1723.23", true);
                var contaDestino = criarConta(null, "001", TipoDeConta.CORRENTE, "Fulado cliente", "0.00", true);

                ajustarSaldos(contaOrigem, contaDestino, dados.saldo());

                List<Conta> contas = new ArrayList<>(Arrays.asList(contaOrigem, contaDestino));

                when(contaService.transferir(1L, 2L, new BigDecimal("1000.00"))).thenReturn(contas);

                var response = realizarPost("/contas/transferencia", dadosTransacaoTransferenciaJt.write(dados).getJson(), "1",
                                "2");

                var jsonEsperado = objectMapper.writeValueAsString(
                                contas.stream().map(DadosDetalhamentoConta::new).collect(Collectors.toList()));

                assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("POST de transferencia inválida deve retornar 400")
        void transferirEntreContasCenarioDois() throws IOException, Exception {
                var dados = criarDadosTransacaoTransferencia("1000.00");

                var contaOrigem = criarConta(null, "001", TipoDeConta.CORRENTE, "Fulado cliente", "723.23", true);
                var contaDestino = criarConta(null, "001", TipoDeConta.CORRENTE, "Fulado cliente", "0.00", true);

                ajustarSaldos(contaOrigem, contaDestino, dados.saldo());

                when(contaService.transferir(1L, 2L, new BigDecimal("1000.00")))
                                .thenThrow(new ValidacaoException("Saldo insuficiente na conta de origem."));

                var response = realizarPost("/contas/transferencia", dadosTransacaoTransferenciaJt.write(dados).getJson(), "1",
                                "2");

                assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.getContentAsString()).contains("Saldo insuficiente na conta de origem.");
        }

        private DadosCadastroContaBancaria criarDadosCadastroContaBancaria(String agencia, TipoDeConta tipo,
                        String titular, String saldo, boolean ativa) {
                return new DadosCadastroContaBancaria(agencia, tipo, titular, new BigDecimal(saldo), ativa);
        }

        private DadosTransacaoTransferencia criarDadosTransacaoTransferencia(String valor) {
                return new DadosTransacaoTransferencia(new BigDecimal(valor));
        }

        private Conta criarConta(Long id, String agencia, TipoDeConta tipo, String titular, String saldo,
                        boolean ativa) {
                return new Conta(id, agencia, tipo, titular, new BigDecimal(saldo), ativa);
        }

        private void ajustarSaldos(Conta contaOrigem, Conta contaDestino, BigDecimal valor) {
                contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
                contaDestino.setSaldo(contaDestino.getSaldo().add(valor));
        }

        private MockHttpServletResponse realizarPost(String url, String jsonContent, String... params)
                        throws Exception {
                var request = post(url).contentType(MediaType.APPLICATION_JSON).content(jsonContent);
                if (params.length == 2) {
                        request = request.param("idOrigem", params[0]).param("idDestino", params[1]);
                }
                return mvc.perform(request).andReturn().getResponse();
        }
}
