package com.algafood.domain.infrastructure.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.algafood.domain.filter.VendaDiariaFilter;
import com.algafood.domain.model.Pedido;
import com.algafood.domain.model.StatusPedido;
import com.algafood.domain.model.dto.VendaDiariaDto;
import com.algafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    // injeta o EntityManager da JPA
    @PersistenceContext
    private EntityManager manager;

    // http://localhost:8080/estatisticas/vendas-diarias?restauranteId=1
    @Override
    public List<VendaDiariaDto> consultarVendasDiarias(VendaDiariaFilter filter) {
        
        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiariaDto.class);
        var root = query.from(Pedido.class);
        var predicates = new ArrayList<Predicate>();

        // essa dataCriacao é um atributo de com.algafood.domain.model.Pedido
        var functionDateDataCriacao = builder.function(
            "date", LocalDate.class, root.get("dataCriacao"));

        var selection = builder.construct(VendaDiariaDto.class, 
            functionDateDataCriacao,
            builder.count(root.get("id")),
            builder.sum(root.get("valorTotal"))
        );

        if (filter.getRestauranteId() != null) {
            predicates.add(builder.equal(root.get("restaurante"), filter.getRestauranteId()));
        }
            
        if (filter.getDataInicio() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), 
                    filter.getDataInicio()));
        }

        if (filter.getDataFim() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), 
                    filter.getDataFim()));
        }
            
        predicates.add(root.get("status").in(
                StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));

        query.select(selection);
        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(functionDateDataCriacao);

        return manager.createQuery(query).getResultList();
    }
    
}
