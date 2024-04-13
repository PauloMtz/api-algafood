package com.algafood.domain.service;

import com.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {
    
    byte[] emitirRelatorioVendasDiarias(
        VendaDiariaFilter filter, String timeOffset);
}
