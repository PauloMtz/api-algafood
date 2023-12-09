package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.model.Estado;
import com.algafood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
    
    @Autowired
    private EstadoRepository repository;

    public Estado salvar(Estado estado) {
        return repository.save(estado);
    }

    public void excluir(Long estadoId) {
        try {
            repository.deleteById(estadoId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntidadeNaoEncontradaException(String.format(
                "Registro de ID %d não encontrado", estadoId));
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                "O registro de ID %d está associado a outra entidade e não pode ser removido.", estadoId));
        }
    }
}
