package com.algafood.domain.repository;

import java.util.List;

import com.algafood.domain.model.Cozinha;

public interface CozinhaRepository {
    
    List<Cozinha> listar();
	Cozinha buscar(Long id);
	List<Cozinha> listaPorNome(String nome);
	Cozinha salvar(Cozinha cozinha);
	void remover(Long id);
}
