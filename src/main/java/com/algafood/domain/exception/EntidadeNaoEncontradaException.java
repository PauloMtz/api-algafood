package com.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Recurso não encontrado")
public class EntidadeNaoEncontradaException extends ResponseStatusException {
    
    // com dois construtores, pode-se chamar um ou outro
    
    public EntidadeNaoEncontradaException(HttpStatus status, String mensagem) {
        super(status, mensagem);
    }

    public EntidadeNaoEncontradaException(String mensagem) {
        this(HttpStatus.NOT_FOUND, mensagem);
    }
}
