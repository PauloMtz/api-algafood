package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.ProdutoDto;
import com.algafood.domain.model.Produto;

@Component
public class ProdutoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public ProdutoDto convertToDto(Produto produto) {
        return modelMapper.map(produto, ProdutoDto.class);
    }
    
    public List<ProdutoDto> convertToCollectionDto(List<Produto> produtos) {
        return produtos.stream()
                .map(Produto -> convertToDto(Produto))
                .collect(Collectors.toList());
    }
}
