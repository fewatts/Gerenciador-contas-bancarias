package com.api.banco.gerenciamento.banco.watts.domain.validation.validadorconta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;
import com.api.banco.gerenciamento.banco.watts.domain.repository.ContaRepository;
import com.api.banco.gerenciamento.banco.watts.domain.validation.ValidacaoException;

@Component
public class ValidarTitularEContaDuplicados implements ValidarCriacaoDeContas{

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public void validar(DadosCadastroContaBancaria dados) {
       if(contaRepository.existsByTitularIgnoreCase(dados.titular()) && contaRepository.existsByTipoDeConta(dados.tipoDeConta())){
            throw new ValidacaoException("Tipo de conta '" + dados.tipoDeConta() + "' para titular '" + dados.titular() + "' já existe.");
       }
    }
    
}
