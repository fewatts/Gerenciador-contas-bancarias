package com.api.banco.gerenciamento.banco.watts.domain.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosTransacaoDeposito;
import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosTransacaoSaque;
import com.api.banco.gerenciamento.banco.watts.domain.model.Conta;
import com.api.banco.gerenciamento.banco.watts.domain.repository.ContaRepository;
import com.api.banco.gerenciamento.banco.watts.domain.validation.ValidacaoException;
import com.api.banco.gerenciamento.banco.watts.domain.validation.validadorconta.ValidarCriacaoDeContas;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private List<ValidarCriacaoDeContas> validadores;

    public Conta criarConta(DadosCadastroContaBancaria dados) {
        validadores.forEach(v -> v.validar(dados));
        var contaNova = new Conta(dados);
        return contaRepository.save(contaNova);
    }

    public Conta depositarEmConta(Long id, DadosTransacaoDeposito dados) {
        var conta = contaRepository.getReferenceById(id);
        conta.setSaldo(conta.getSaldo().add(dados.saldo()));
        return contaRepository.save(conta);
    }

    public Conta sacar(Long id, DadosTransacaoSaque dados) {
        Conta conta = contaRepository.getReferenceById(id);
        if (conta.getSaldo().compareTo(dados.saldo()) >= 0) {
            conta.setSaldo(conta.getSaldo().subtract(dados.saldo()));
            return contaRepository.save(conta);
        } else {
            throw new ValidacaoException("Saldo insuficiente.");
        }
    }

    public Conta encerrarConta(Long id) {
        Conta conta = contaRepository.getReferenceById(id);
        conta.setAtiva(false);
        return contaRepository.save(conta);
    }

    public List<Conta> transferir(Long idOrigem, Long idDestino, BigDecimal valor) {
        Conta contaOrigem = contaRepository.getReferenceById(idOrigem);
        Conta contaDestino = contaRepository.getReferenceById(idDestino);

        if (contaOrigem.getSaldo().compareTo(valor) >= 0) {
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
            contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

            return Arrays.asList(contaRepository.save(contaOrigem), contaRepository.save(contaDestino));
        } else {
            throw new ValidacaoException("Saldo insuficiente na conta de origem.");
        }
    }
}
