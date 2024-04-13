package com.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.domain.filter.VendaDiariaFilter;
import com.algafood.domain.model.dto.VendaDiariaDto;
import com.algafood.domain.service.VendaQueryService;
import com.algafood.domain.service.VendaReportService;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {
    
    @Autowired
    private VendaQueryService vendaQueryService;

    @Autowired
    private VendaReportService vendaReportService;

    // http://localhost:8080/estatisticas/vendas-diarias?dataInicio=2019-10-30T00:00:00Z&dataFim=2019-11-04T23:59:59Z
    // http://localhost:8080/estatisticas/vendas-diarias?dataInicio=2019-10-30T00:00:00Z&dataFim=2019-11-04T23:59:59Z&timeOffset=-03:00
    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiariaDto> consultarVendasDiarias(VendaDiariaFilter filter, 
        @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        
        return vendaQueryService.consultarVendasDiarias(filter, timeOffset);
    }

    // path e value dá na mesma
    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasRelatorio(VendaDiariaFilter filter, 
        @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        
        byte[] bytesRelatorio = vendaReportService.emitirRelatorioVendasDiarias(filter, timeOffset);

        var fileHeaders = new HttpHeaders();
        // esse attachment indica que é para download
        fileHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .headers(fileHeaders)
            .body(bytesRelatorio);
    }
}
