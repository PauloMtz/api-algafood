package com.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.model.FotoProduto;
import com.algafood.domain.repository.ProdutoRepository;

@Service
public class ProdutoUploadService {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Transactional
    public FotoProduto salvar(FotoProduto fotoProduto) {

        Long restauranteId = fotoProduto.getRestauranteId();
        Long produtoId = fotoProduto.getProduto().getId();

        Optional<FotoProduto> fotoExistente = produtoRepository
            .findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            // se já tiver uma foto, exclui
            produtoRepository.delete(fotoProduto);
        }

        // se já tiver uma foto, exclui para salvar outra em cima
        return produtoRepository.save(fotoProduto);
    }
}
