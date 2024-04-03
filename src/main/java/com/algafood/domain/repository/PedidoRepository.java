package com.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algafood.domain.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>,
    JpaSpecificationExecutor<Pedido> {

    // não precisa dessa query porque já tem esse atributo na classe
    //@Query("from Pedido where codigo = :codigo")
    Optional<Pedido> findByCodigo(String codigo);

    @Query("from Pedido p join fetch p.cliente join fetch p.restaurante r "
        + "join fetch r.cozinha")
    List<Pedido> findAll();
}
