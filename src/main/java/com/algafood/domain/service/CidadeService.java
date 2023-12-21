package com.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.model.Cidade;
import com.algafood.domain.model.Estado;
import com.algafood.domain.repository.CidadeRepository;
import com.algafood.domain.repository.EstadoRepository;

@Service
public class CidadeService {
    
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade) {
        // recebe um estado associado no corpo da requisição
        Long estadoId = cidade.getEstado().getId();
        // busca o registro no banco
        Optional<Estado> estado = estadoRepository.findById(estadoId);

        // se o registron for nulo, retorna mensagem de erro
        if (estado.isEmpty()) {
            throw new EntidadeNaoEncontradaException(String.format(
                "Não existe estado com o ID %d", estadoId));
        }

        // se encontrar o registro, define o registro associado
        cidade.setEstado(estado.get());

        // salva no banco
        return cidadeRepository.save(cidade);
    }

    public void excluir(Long cidadeId) {
        try {
            cidadeRepository.deleteById(cidadeId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntidadeNaoEncontradaException(String.format(
                "Registro de ID %d não encontrado", cidadeId));
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                "O registro de ID %d está associado a outra entidade e não pode ser removido.", cidadeId));
        }
	}
}
