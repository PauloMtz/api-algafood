package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.ProdutoFotoDto;
import com.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public ProdutoFotoDto convertToDto(FotoProduto fotoProduto) {
        return modelMapper.map(fotoProduto, ProdutoFotoDto.class);
    }
}
