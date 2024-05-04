package com.algafood.domain.infrastructure.storage;

import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public void remover(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            Files.deleteIfExists(arquivoPath);
        } catch (IOException e) {
            throw new StorageException("Erro ao remover arquivo", e);
        }
    }

    @Override
    public InputStream recuperar(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            return Files.newInputStream(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Erro ao recuperar arquivo.", e);
        }
    }

    @Override
    public void substituirArquivo(String fotoExistenteArmazenamento, NovaFoto novaFoto) {

        this.armazenar(novaFoto);

        // antes de armazenar em disco, remover a anterior
        if (fotoExistenteArmazenamento != null) {
            this.remover(fotoExistenteArmazenamento);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return diretorioFotos.resolve(Path.of(nomeArquivo));
    }
}
