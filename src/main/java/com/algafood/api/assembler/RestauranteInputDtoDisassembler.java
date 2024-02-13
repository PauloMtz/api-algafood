package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.RestauranteInputDto;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDtoDisassembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public Restaurante convertToDomainObject(RestauranteInputDto restauranteInputDto) {
        
        return modelMapper.map(restauranteInputDto, Restaurante.class);
    }

    public void copyToDomainObject(RestauranteInputDto restauranteInputDto,
        Restaurante restaurante) {
        
        /* para evitar org.springframework.orm.jpa.JpaSystemException: 
        identifier of an instance of com.algafood.domain.model.Cozinha 
        was altered from 1 to 2; nested exception is org.hibernate.
        HibernateException: identifier of an instance of 
        com.algafood.domain.model.Cozinha was altered from 1 to 2]*/
        restaurante.setCozinha(new Cozinha());

        modelMapper.map(restauranteInputDto, restaurante);
    }
}
