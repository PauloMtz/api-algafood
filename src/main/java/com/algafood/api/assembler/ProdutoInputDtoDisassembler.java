package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.ProdutoInputDto;
import com.algafood.domain.model.Produto;

@Component
public class ProdutoInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Produto convertToDomainObject(ProdutoInputDto produtoInputDto) {
        return modelMapper.map(produtoInputDto, Produto.class);
    }
    
    public void copyToDomainObject(ProdutoInputDto produtoInputDto, Produto produto) {
        modelMapper.map(produtoInputDto, produto);
    }
}
