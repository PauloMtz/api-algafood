package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.RestauranteInputDto;
import com.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDtoDisassembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public Restaurante convertToDomainObject(RestauranteInputDto restauranteInputDto) {
        
        return modelMapper.map(restauranteInputDto, Restaurante.class);
    }
}
