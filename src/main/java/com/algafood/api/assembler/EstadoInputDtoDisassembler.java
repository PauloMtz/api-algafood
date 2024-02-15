package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.EstadoInputDto;
import com.algafood.domain.model.Estado;

@Component
public class EstadoInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Estado convertToDomainObject(EstadoInputDto estadoInputDto) {
        return modelMapper.map(estadoInputDto, Estado.class);
    }
    
    public void copyToDomainObject(EstadoInputDto estadoInputDto, Estado estado) {
        modelMapper.map(estadoInputDto, estado);
    }
}
