package com.api.banco.gerenciamento.banco.watts.domain.dto;

import java.math.BigDecimal;

import com.api.banco.gerenciamento.banco.watts.domain.model.Conta;
import com.api.banco.gerenciamento.banco.watts.domain.model.TipoDeConta;

public record DadosDetalhamentoConta(
        Long id,
        String agencia,
        TipoDeConta tipoDeConta,
        String titular,
        BigDecimal saldo,
        boolean ativa
    ) {
    public DadosDetalhamentoConta(Conta conta) {
        this(conta.getId(), conta.getAgencia(), conta.getTipoDeConta(), conta.getTitular(), conta.getSaldo(),
                conta.isAtiva());
    }
}
