package com.algafood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum TypeMessage {
    
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado-exception", 
        "Recurso não encontrado"),
    RECURSO_NAO_PODE_SER_REMOVIDO("/nao-pode-ser-removido", 
        "Recurso não pode ser removido"),
    RECURSO_INVALIDO("/recurso-invalido", 
        "Recurso e/ou informação inválidos"),
    RECURSO_NAO_IDENTIFICADO("/nao-identificado", 
        "Recurso não identificado");

    private String title;
    private String uri;

    private TypeMessage(String path, String title) {
        this.uri = "http://localhost:8080" + path;
        this.title = title;
    }
}
