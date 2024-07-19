package com.api.banco.gerenciamento.banco.watts.domain.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;

public record DadosTransacaoSaque(
    @DecimalMin(value = "0.0", inclusive = false)
    @JsonProperty("saque")
    BigDecimal saldo
) {}
