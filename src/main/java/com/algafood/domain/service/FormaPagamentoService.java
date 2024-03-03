package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algafood.domain.model.FormaPagamento;
import com.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {

    private static final String MSG_REGISTRO_UTILIZADO = 
        "O registro de ID %d está associado a outra entidade e não pode ser removido.";
    
    @Autowired
    private FormaPagamentoRepository repository;

    public FormaPagamento salvar(FormaPagamento formaPagamento) {

        return repository.save(formaPagamento);
    }

    public void excluir(Long formaPagamentoId) {
        try {
            repository.deleteById(formaPagamentoId);
        } catch (EmptyResultDataAccessException ex) {
            throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                MSG_REGISTRO_UTILIZADO, formaPagamentoId));
        }
	}

    public FormaPagamento buscar(Long formaPagamentoId) {
		return repository.findById(formaPagamentoId)
			.orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
	}
}
