package com.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.FormaPagamentoDto;
import com.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public FormaPagamentoDto convertToDto(FormaPagamento formaPagamento) {
        return modelMapper.map(formaPagamento, FormaPagamentoDto.class);
    }
    
    public List<FormaPagamentoDto> convertToCollectionDto(Collection<FormaPagamento> formasPagamentos) {
        return formasPagamentos.stream()
            .map(formaPagamento -> convertToDto(formaPagamento))
            .collect(Collectors.toList());
    }
}
