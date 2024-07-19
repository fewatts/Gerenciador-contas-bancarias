package com.api.banco.gerenciamento.banco.watts.domain.validation.validadorconta;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;

public interface ValidarCriacaoDeContas {

    void validar(DadosCadastroContaBancaria dados);

}
