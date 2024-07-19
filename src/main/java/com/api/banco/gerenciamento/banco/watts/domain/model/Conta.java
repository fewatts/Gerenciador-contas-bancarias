package com.api.banco.gerenciamento.banco.watts.domain.model;

import java.math.BigDecimal;

import com.api.banco.gerenciamento.banco.watts.domain.dto.DadosCadastroContaBancaria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String agencia;

    @Column(name = "tipo_de_conta")
    @Enumerated(EnumType.STRING)
    private TipoDeConta tipoDeConta;

    private String titular;

    private BigDecimal saldo;
    
    private boolean ativa;
    
    public Conta(DadosCadastroContaBancaria dados) {
        this.agencia = dados.agencia();
        this.tipoDeConta = dados.tipoDeConta();
        this.titular = dados.titular();
        this.saldo = dados.saldo();
        this.ativa = dados.ativa();
    }
}
