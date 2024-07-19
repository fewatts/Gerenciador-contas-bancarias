package com.api.banco.gerenciamento.banco.watts.domain.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;

public record DadosTransacaoDeposito(
    @DecimalMin(value = "0.0", inclusive = false)
    @JsonProperty("deposito")
    BigDecimal saldo
) {}
