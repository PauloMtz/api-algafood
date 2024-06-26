package com.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algafood.domain.model.FotoProduto;
import com.algafood.domain.model.Produto;
import com.algafood.domain.model.Restaurante;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    @Query("from Produto where restaurante.id = :restauranteId and id = :produtoId")
    Optional<Produto> findById(@Param("restauranteId") Long restauranteId, 
        @Param("produtoId") Long produtoId);
    
    List<Produto> findProdutosByRestaurante(Restaurante restaurante);

    @Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
    List<Produto> findProdutosAtivosByRestaurante(Restaurante restaurante);

    @Query("select fp from FotoProduto fp join fp.produto p "
        + "where p.restaurante.id = :restauranteId "
        + "and fp.id = :produtoId")
    Optional<FotoProduto> findFotoById(Long restauranteId, Long produtoId);
}
