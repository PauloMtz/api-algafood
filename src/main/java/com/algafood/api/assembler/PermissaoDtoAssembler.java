package com.algafood.api.assembler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.PermissaoDto;
import com.algafood.domain.model.Permissao;

@Component
public class PermissaoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public PermissaoDto convertToDto(Permissao permissao) {
        return modelMapper.map(permissao, PermissaoDto.class);
    }
    
    public List<PermissaoDto> convertToCollectionDto(Set<Permissao> set) {
        return set.stream()
                .map(permissao -> convertToDto(permissao))
                .collect(Collectors.toList());
    }
}
