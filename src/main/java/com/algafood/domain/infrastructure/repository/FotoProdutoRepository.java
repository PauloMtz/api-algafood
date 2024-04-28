package com.algafood.domain.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.model.FotoProduto;
import com.algafood.domain.repository.ProdutoFotoRepository;

@Repository
public class FotoProdutoRepository implements ProdutoFotoRepository {
    
    @PersistenceContext
    private EntityManager manager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto fotoProduto) {
        return manager.merge(fotoProduto);
    }
}
