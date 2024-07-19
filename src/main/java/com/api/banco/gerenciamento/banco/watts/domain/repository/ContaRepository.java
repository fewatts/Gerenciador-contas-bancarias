package com.api.banco.gerenciamento.banco.watts.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.banco.gerenciamento.banco.watts.domain.model.Conta;
import com.api.banco.gerenciamento.banco.watts.domain.model.TipoDeConta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>{

    boolean existsByTitularIgnoreCase(String titular);

    boolean existsByTipoDeConta(TipoDeConta tipoDeConta);

    Page<Conta> findByAtivaTrue(Pageable paginacao);

}
