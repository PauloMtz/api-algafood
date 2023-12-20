package com.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeNaoRemoverException extends RuntimeException {
    
    public EntidadeNaoRemoverException(String mensagem) {
        super(mensagem);
    }
}
