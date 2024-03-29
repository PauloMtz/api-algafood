package com.algafood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum TypeMessage {
    
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", 
        "Mensagem incompreensível"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", 
        "Recurso não encontrado"),
    RECURSO_NAO_PODE_SER_REMOVIDO("/nao-pode-ser-removido", 
        "Recurso não pode ser removido"),
    RECURSO_INVALIDO("/recurso-invalido", 
        "Recurso e/ou informação inválidos"),
    RECURSO_NAO_IDENTIFICADO("/nao-identificado", 
        "Recurso não identificado"),
    PARAMETRO_INVALIDO("/invalido", 
        "Parâmetro informado está inválido");

    private String title;
    private String uri;

    private TypeMessage(String path, String title) {
        this.uri = "http://localhost:8080" + path;
        this.title = title;
    }
}
