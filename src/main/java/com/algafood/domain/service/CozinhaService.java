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

    private static final String MSG_REGISTRO_UTILIZADO = 
        "O registro de ID %d está associado a outra entidade e não pode ser removido.";

	private static final String MSG_RECURSO_NAO_ENCONTRADO = 
        "Registro de ID %d não encontrado";
    
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
                MSG_RECURSO_NAO_ENCONTRADO, cozinhaId));
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                MSG_REGISTRO_UTILIZADO, cozinhaId));
        }
    }

    public Cozinha buscar(Long cozinhaId) {
		return repository.findById(cozinhaId)
			.orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format(MSG_RECURSO_NAO_ENCONTRADO, cozinhaId)));
	}
}
