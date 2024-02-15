package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.EstadoDto;
import com.algafood.domain.model.Estado;

@Component
public class EstadoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public EstadoDto convertToDto(Estado estado) {
        return modelMapper.map(estado, EstadoDto.class);
    }
    
    public List<EstadoDto> convertToCollectionDto(List<Estado> estados) {
        return estados.stream()
                .map(estado -> convertToDto(estado))
                .collect(Collectors.toList());
    }
}
