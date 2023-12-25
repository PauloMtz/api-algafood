package com.algafood.domain.exception;

public class EntidadeNaoRemoverException extends RuntimeException {
    
    public EntidadeNaoRemoverException(String mensagem) {
        super(mensagem);
    }
}
