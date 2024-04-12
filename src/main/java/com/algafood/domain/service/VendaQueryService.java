package com.algafood.domain.service;

import java.util.List;

import com.algafood.domain.filter.VendaDiariaFilter;
import com.algafood.domain.model.dto.VendaDiariaDto;

public interface VendaQueryService {
    
    List<VendaDiariaDto> consultarVendasDiarias(
        VendaDiariaFilter filter, String timeOffset);
}
