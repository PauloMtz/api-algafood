package com.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.algafood.domain.model.Produto;
import com.algafood.domain.model.Restaurante;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    @Query("from Produto where restaurante.id = :restauranteId and id = :produtoId")
    Optional<Produto> findById(@Param("restauranteId") Long restauranteId, 
        @Param("produtoId") Long produtoId);
    
    List<Produto> findByRestaurante(Restaurante restaurante);
}
