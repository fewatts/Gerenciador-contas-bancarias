package com.api.banco.gerenciamento.banco.watts.domain.dto;

import java.math.BigDecimal;

import com.api.banco.gerenciamento.banco.watts.domain.model.TipoDeConta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroContaBancaria(
    @NotBlank String agencia,
    @NotNull TipoDeConta tipoDeConta,
    @NotBlank String titular,
    
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal saldo,
    
    @NotNull boolean ativa
) {}
