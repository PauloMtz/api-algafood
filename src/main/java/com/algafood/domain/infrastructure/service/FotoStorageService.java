package com.algafood.domain.infrastructure.service;

import java.io.InputStream;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {
    
    void armazenar(NovaFoto novaFoto);
    String gerarNomeArquivo(String nomeArquivo);

    @Builder
    @Getter
    class NovaFoto {

        private String nomeArquivo;
        private InputStream fluxoArquivo;
    }
}
