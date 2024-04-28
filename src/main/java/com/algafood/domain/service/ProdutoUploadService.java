package com.algafood.domain.service;

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
        return produtoRepository.save(fotoProduto);
    }
}
