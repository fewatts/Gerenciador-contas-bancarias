package com.api.banco.gerenciamento.banco.watts.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosTransacaoDeposito;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosTransacaoSaque;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosTransacaoTransferencia;
import com.api.banco.gerenciamento.banco.watts.domain.model.Conta;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosDetalhamentoConta;
import com.api.banco.gerenciamento.banco.watts.domain.repository.ContaRepository;
import com.api.banco.gerenciamento.banco.watts.domain.service.ContaService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaRepository contaRepository;

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(
            @PageableDefault(size = 10, sort = { "titular" }, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = contaRepository.findByAtivaTrue(paginacao).map(DadosDetalhamentoConta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<DadosDetalhamentoConta> consultarSaldo(@PathVariable Long id) {
        var conta = contaRepository.getReferenceById(id);
        return ResponseEntity.ok().body(new DadosDetalhamentoConta(conta));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoConta> criarConta(@RequestBody @Valid DadosCadastroContaBancaria dados,
            UriComponentsBuilder uriBuilder) {
        var novaConta = contaService.criarConta(dados);
        var uri = uriBuilder.path("/contas/{id}").buildAndExpand(novaConta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoConta(novaConta));
    }

    @PostMapping("/{id}/deposito")
    @Transactional
    public ResponseEntity<DadosDetalhamentoConta> depositarEmConta(@PathVariable Long id,
            @RequestBody DadosTransacaoDeposito dados) {
        var conta = contaService.depositarEmConta(id, dados);
        return ResponseEntity.ok().body(new DadosDetalhamentoConta(conta));
    }

    @PostMapping("/{id}/saque")
    @Transactional
    public ResponseEntity<DadosDetalhamentoConta> sacar(@PathVariable Long id, @RequestBody DadosTransacaoSaque dados) {
        var conta = contaService.sacar(id, dados);
        return ResponseEntity.ok().body(new DadosDetalhamentoConta(conta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoConta> detalharConta(@PathVariable Long id) {
        var conta = contaRepository.getReferenceById(id);
        return ResponseEntity.ok().body(new DadosDetalhamentoConta(conta));
    }

    @PostMapping("/transferencia")
    @Transactional
    public ResponseEntity<List<DadosDetalhamentoConta>> transferir(
            @RequestParam Long idOrigem,
            @RequestParam Long idDestino,
            @RequestBody DadosTransacaoTransferencia dados) {
        List<Conta> contas = contaService.transferir(idOrigem, idDestino, dados.saldo());

        List<DadosDetalhamentoConta> dadosDetalhamentoContas = contas.stream()
                .map(DadosDetalhamentoConta::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dadosDetalhamentoContas);
    }

    @DeleteMapping("/{id}/encerrar")
    @Transactional
    public ResponseEntity<HttpStatus> encerrarConta(@PathVariable Long id) {
        contaService.encerrarConta(id);
        return ResponseEntity.noContent().build();
    }
}
