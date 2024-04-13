package com.algafood.domain.infrastructure.service;

public class VendaReportException extends RuntimeException {
    
    public VendaReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public VendaReportException(String message) {
        super(message);
    }
}
