package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.CozinhaDto;
import com.algafood.domain.model.Cozinha;

@Component
public class CozinhaDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public CozinhaDto convertToDto(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaDto.class);
    }
    
    public List<CozinhaDto> convertToCollectionDto(List<Cozinha> cozinhas) {
        return cozinhas.stream()
                .map(cozinha -> convertToDto(cozinha))
                .collect(Collectors.toList());
    }
}
