package com.algafood.domain.infrastructure.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algafood.domain.filter.VendaDiariaFilter;
import com.algafood.domain.model.Pedido;
import com.algafood.domain.model.dto.VendaDiariaDto;
import com.algafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    // injeta o EntityManager da JPA
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<VendaDiariaDto> consultarVendasDiarias(VendaDiariaFilter filter) {
        
        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiariaDto.class);
        var root = query.from(Pedido.class);

        var functionDateDataCriacao = builder.function(
            "date", LocalDate.class, root.get("dataCriacao"));

        var selection = builder.construct(VendaDiariaDto.class, 
            functionDateDataCriacao,
            builder.count(root.get("id")),
            builder.sum(root.get("valorTotal"))
        );

        query.select(selection);
        query.groupBy(functionDateDataCriacao);

        return manager.createQuery(query).getResultList();
    }
    
}
