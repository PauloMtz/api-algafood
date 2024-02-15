package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.CozinhaInputDto;
import com.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Cozinha convertToDomainObject(CozinhaInputDto cozinhaInputDto) {
        return modelMapper.map(cozinhaInputDto, Cozinha.class);
    }
    
    public void copyToDomainObject(CozinhaInputDto cozinhaInputDto, Cozinha cozinha) {
        modelMapper.map(cozinhaInputDto, cozinha);
    }
}
