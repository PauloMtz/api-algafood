package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.GrupoInputDto;
import com.algafood.domain.model.Grupo;

@Component
public class GrupoInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Grupo convertToDomainObject(GrupoInputDto grupoInputDto) {
        return modelMapper.map(grupoInputDto, Grupo.class);
    }
    
    public void copyToDomainObject(GrupoInputDto grupoInputDto, Grupo grupo) {
        modelMapper.map(grupoInputDto, grupo);
    }
}
