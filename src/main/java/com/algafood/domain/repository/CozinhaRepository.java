package com.algafood.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

	@Query("FROM Cozinha f WHERE UPPER(f.nome) LIKE CONCAT('%', UPPER(:nome), '%')")
	List<Cozinha> listaPorNome(@Param("nome") String nome);
}
