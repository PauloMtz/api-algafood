package com.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.GrupoDto;
import com.algafood.domain.model.Grupo;

@Component
public class GrupoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public GrupoDto convertToDto(Grupo grupo) {
        return modelMapper.map(grupo, GrupoDto.class);
    }
    
    public List<GrupoDto> convertToCollectionDto(Collection<Grupo> grupos) {
        return grupos.stream()
            .map(grupo -> convertToDto(grupo))
            .collect(Collectors.toList());
    } 
}
