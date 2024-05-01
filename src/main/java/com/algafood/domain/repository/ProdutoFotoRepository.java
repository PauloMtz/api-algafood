package com.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algafood.domain.model.FotoProduto;

@Repository
public interface ProdutoFotoRepository extends JpaRepository<FotoProduto, Long> {
}
