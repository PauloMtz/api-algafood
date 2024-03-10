package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algafood.domain.model.Permissao;
import com.algafood.domain.repository.PermissaoRepository;

@Service
public class PermissaoService {
    
    @Autowired
    private PermissaoRepository repository;
    
    public Permissao buscar(Long permissaoId) {
        return repository.findById(permissaoId)
            .orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }
}
