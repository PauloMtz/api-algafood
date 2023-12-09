package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;

@Service
public class CozinhaService {
    
    @Autowired
    private CozinhaRepository repository;

    public Cozinha salvar(Cozinha cozinha) {
        return repository.save(cozinha);
    }

    public void excluir(Long cozinhaId) {
        try {
            repository.deleteById(cozinhaId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntidadeNaoEncontradaException(String.format(
                "Registro de ID %d não encontrado", cozinhaId));
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                "O registro de ID %d está associado a outra entidade e não pode ser removido.", cozinhaId));
        }
    }
}
