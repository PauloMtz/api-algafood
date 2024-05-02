package com.algafood.domain.infrastructure.service;

import java.io.InputStream;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {
    
    void armazenar(NovaFoto novaFoto);
    void remover(String nomeArquivo);
    InputStream recuperar(String nomeArquivo);
    String gerarNomeArquivo(String nomeArquivo);
    void substituirArquivo(String fotoExistenteArmazenamento, NovaFoto novaFoto);

    @Builder
    @Getter
    class NovaFoto {

        private String nomeArquivo;
        private InputStream fluxoArquivo;
    }
}
