package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.FormaPagamentoInputDto;
import com.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public FormaPagamento convertToDomainObject(FormaPagamentoInputDto formaPagamentoInput) {
        return modelMapper.map(formaPagamentoInput, FormaPagamento.class);
    }
    
    public void copyToDomainObject(FormaPagamentoInputDto formaPagamentoInput, FormaPagamento formaPagamento) {
        modelMapper.map(formaPagamentoInput, formaPagamento);
    } 
}
