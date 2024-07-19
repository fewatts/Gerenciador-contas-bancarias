package com.api.banco.gerenciamento.banco.watts.infra.exception;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.api.banco.gerenciamento.banco.watts.domain.validation.ValidacaoException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpStatus> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> tratarErroDeArgumento(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Argumento incorreto: " + ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratarErro400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> tratarErroRegraDeNegocio(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Stream<Object>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpStatus> tratarErroElementoNaoEncontrado(NoSuchElementException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErroArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
