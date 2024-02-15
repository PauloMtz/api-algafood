package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.CidadeDto;
import com.algafood.domain.model.Cidade;

@Component
public class CidadeDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public CidadeDto convertToDto(Cidade cidade) {
        return modelMapper.map(cidade, CidadeDto.class);
    }
    
    public List<CidadeDto> convertToCollectionDto(List<Cidade> cidades) {
        return cidades.stream()
                .map(cidade -> convertToDto(cidade))
                .collect(Collectors.toList());
    }
}
