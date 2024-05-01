package com.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.infrastructure.service.FotoStorageService;
import com.algafood.domain.infrastructure.service.FotoStorageService.NovaFoto;
import com.algafood.domain.model.FotoProduto;
import com.algafood.domain.repository.ProdutoFotoRepository;
import com.algafood.domain.repository.ProdutoRepository;

@Service
public class ProdutoUploadService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoFotoRepository fotoRepository;

    @Autowired
    private FotoStorageService storageService;
    
    @Transactional
    public FotoProduto salvar(FotoProduto fotoProduto, InputStream dadosArquivo) {

        Long restauranteId = fotoProduto.getRestauranteId();
        Long produtoId = fotoProduto.getProduto().getId();
        String novoNomeArquivo = storageService.gerarNomeArquivo(fotoProduto.getNomeArquivo());

        Optional<FotoProduto> fotoExistente = produtoRepository
            .findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            // se já tiver uma foto, exclui
            fotoRepository.delete(fotoExistente.get());
        }

        // salva primeiro e atualiza JPA, porque se der erro
        // não armazenará arquivo, e seria mais complicado
        // fazer rollback do armazenamento do arquivo
        fotoProduto.setNomeArquivo(novoNomeArquivo);
        fotoProduto = fotoRepository.save(fotoProduto);
        fotoRepository.flush();

        NovaFoto novaFoto = NovaFoto.builder()
            .nomeArquivo(fotoProduto.getNomeArquivo())
            .fluxoArquivo(dadosArquivo)
            .build();

        storageService.armazenar(novaFoto);

        // se já tiver uma foto, exclui para salvar outra em cima
        return fotoProduto;
    }
}
