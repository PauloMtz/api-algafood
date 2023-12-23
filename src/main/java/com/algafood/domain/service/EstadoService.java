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

    private static final String MSG_REGISTRO_UTILIZADO = 
        "O registro de ID %d está associado a outra entidade e não pode ser removido.";

	private static final String MSG_RECURSO_NAO_ENCONTRADO = 
        "Registro de ID %d não encontrado";
    
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
                MSG_RECURSO_NAO_ENCONTRADO, estadoId));
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                MSG_REGISTRO_UTILIZADO, estadoId));
        }
    }

    public Estado buscar(Long cidadeId) {
		return repository.findById(cidadeId)
			.orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format(MSG_RECURSO_NAO_ENCONTRADO, cidadeId)));
	}
}
