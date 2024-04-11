package com.algafood.domain.repository.specification;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algafood.domain.filter.PedidoFilter;
import com.algafood.domain.model.Pedido;

public class PedidoSpecification {
    
    public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {

        return (root, query, builder) -> {

            // para resolver o problema de N + 1
            // ou seja, para cada item associado, a JPA executa um select
            // equivale a (from Pedido p join fetch p.restaurante r 
            //  join fetch r.cozinha join fetch p.cliente)
            if (Pedido.class.equals(query.getResultType())) {
                root.fetch("restaurante").fetch("cozinha");
                root.fetch("cliente");
            }

            var predicates = new ArrayList<Predicate>();

            if (filtro.getClienteId() != null) {
                // esse cliente vem lá da classe Pedido (que é um tipo Usuario)
                predicates.add(builder.equal(
                    root.get("cliente"), filtro.getClienteId()));
            }

            if (filtro.getRestauranteId() != null) {
                predicates.add(builder.equal(
                    root.get("restaurante"), filtro.getRestauranteId()));
            }

            if (filtro.getDataInicio() != null) {
                predicates.add(builder.greaterThanOrEqualTo(
                    root.get("dataCriacao"), filtro.getDataInicio()));
            }

            if (filtro.getDataFim() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                    root.get("dataCriacao"), filtro.getDataFim()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
