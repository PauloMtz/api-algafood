package com.algafood.domain.infrastructure.service;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.algafood.domain.filter.VendaDiariaFilter;
import com.algafood.domain.service.VendaQueryService;
import com.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class VendaReportServiceImpl implements VendaReportService {

    @Autowired
    private VendaQueryService vendaQueryService;

    @Override
    public byte[] emitirRelatorioVendasDiarias(VendaDiariaFilter filter, 
        String timeOffset) {

        try {
            var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas.jasper");
            var parametros = new HashMap<String, Object>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
            var vendasDiarias = vendaQueryService.consultarVendasDiarias(filter, timeOffset);
            var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
            var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new VendaReportException("Não foi possível carregar o relatório", e);
        }
    }
}
