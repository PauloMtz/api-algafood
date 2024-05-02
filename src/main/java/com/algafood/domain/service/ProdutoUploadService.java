package com.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.exception.FotoProdutoNaoEncontradaException;
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
        String fotoExistenteArmazenamento = null;

        Optional<FotoProduto> fotoExistente = produtoRepository
            .findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            // verifica se tem foto armazenada em algum local
            fotoExistenteArmazenamento = fotoExistente.get().getNomeArquivo();

            // se já tiver uma foto no banco, exclui
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

        // esse substituir grava a nova e já remover a anterior se tiver
        storageService.substituirArquivo(fotoExistenteArmazenamento, novaFoto);

        // grava a nova foto
        //storageService.armazenar(novaFoto);

        return fotoProduto;
    }

    public FotoProduto buscar(Long restauranteId, Long produtoId) {
        return produtoRepository.findFotoById(restauranteId, produtoId)
            .orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
    }
}
