package com.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;

@Repository
public class CozinhaRepositoryImpl implements CozinhaRepository {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Cozinha> listar() {
		return manager.createQuery("from Cozinha", Cozinha.class)
			.getResultList();
	}
	
	@Override
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}
	
	@Transactional
	@Override
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);
	}
	
	@Transactional
	@Override
	public void remover(Long id) {
		Cozinha cozinha = buscar(id);

		if (cozinha == null) {
			// o parâmetro do construtor informa que espera pelo menos 1 registro
			throw new EmptyResultDataAccessException(1);
		}
		
		manager.remove(cozinha);
	}

	@Override
	public List<Cozinha> listaPorNome(String nomeCozinha) {
		return manager
			.createQuery("from Cozinha where nome like :nome", Cozinha.class)
			.setParameter("nome", "%" + nomeCozinha + "%")
			.getResultList();
	}
}
