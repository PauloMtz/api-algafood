package com.algafood.domain.infrastructure.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algafood.domain.infrastructure.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

    @Value("${algafood.storage.local.diretorio-fotos}")
    private Path diretorioFotos;

    public String gerarNomeArquivo(String nomeArquivo) {
        int lastIndex = nomeArquivo.lastIndexOf(".");
        String ext = nomeArquivo.substring(lastIndex);
        return UUID.randomUUID().toString().replace("-", "")
            + LocalDateTime.now().toString().replaceAll("[\\:\\-\\.]", "") 
            + ext;
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {

        try {
            Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

            FileCopyUtils.copy(novaFoto.getFluxoArquivo(), 
                Files.newOutputStream(arquivoPath));
        } catch (IOException e) {
            throw new StorageException("Erro ao carregar arquivo", e);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return diretorioFotos.resolve(Path.of(nomeArquivo));
    }
}
