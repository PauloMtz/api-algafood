package com.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.domain.filter.VendaDiariaFilter;
import com.algafood.domain.model.dto.VendaDiariaDto;
import com.algafood.domain.service.VendaQueryService;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {
    
    @Autowired
    private VendaQueryService vendaQueryService;

    // http://localhost:8080/estatisticas/vendas-diarias?dataInicio=2019-10-30T00:00:00Z&dataFim=2019-11-04T23:59:59Z
    // http://localhost:8080/estatisticas/vendas-diarias?dataInicio=2019-10-30T00:00:00Z&dataFim=2019-11-04T23:59:59Z&timeOffset=-03:00
    @GetMapping("/vendas-diarias")
    public List<VendaDiariaDto> consultarVendasDiarias(VendaDiariaFilter filter, 
        @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        
        return vendaQueryService.consultarVendasDiarias(filter, timeOffset);
    }
}
