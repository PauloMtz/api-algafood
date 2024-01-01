package com.algafood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum TypeMessage {
    
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado-exception", "Recurso não encontrado");

    private String title;
    private String uri;

    private TypeMessage(String path, String title) {
        this.uri = "http://localhost:8080" + path;
        this.title = title;
    }
}
