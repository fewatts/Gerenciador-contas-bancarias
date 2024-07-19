package com.api.banco.gerenciamento.banco.watts.domain.validation.validadorconta;

import org.springframework.stereotype.Component;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;
import com.api.banco.gerenciamento.banco.watts.domain.validation.ValidacaoException;

@Component
public class ValidarNumeroDeAgencia implements ValidarCriacaoDeContas{

    @Override
    public void validar(DadosCadastroContaBancaria dados) {
        if(!"001".equals(dados.agencia())){
            throw new ValidacaoException("Nosso Banco possui apenas a agência '001', Verifique sua entrada.");
        }
    }

}
