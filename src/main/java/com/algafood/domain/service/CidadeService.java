package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.model.Cidade;
import com.algafood.domain.model.Estado;
import com.algafood.domain.repository.CidadeRepository;

@Service
public class CidadeService {

    private static final String MSG_REGISTRO_UTILIZADO = 
        "O registro de ID %d está associado a outra entidade e não pode ser removido.";
    
    @Autowired
    private CidadeRepository repository;

    @Autowired
    private EstadoService service;

    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();
        
        Estado estado = service.buscar(estadoId);

        cidade.setEstado(estado);

        return repository.save(cidade);
    }

    public void excluir(Long cidadeId) {
        try {
            repository.deleteById(cidadeId);
        } catch (EmptyResultDataAccessException ex) {
            throw new CidadeNaoEncontradaException(cidadeId);
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                MSG_REGISTRO_UTILIZADO, cidadeId));
        }
	}

    public Cidade buscar(Long cidadeId) {
		return repository.findById(cidadeId)
			.orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}
}
