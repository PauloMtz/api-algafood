package com.algafood.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.UsuarioDto;
import com.algafood.domain.model.Usuario;

@Component
public class UsuarioDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public UsuarioDto convertToDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDto.class);
    }
    
    public List<UsuarioDto> convertToCollectionDto(Collection<Usuario> usuarios) {
        return usuarios.stream()
            .map(usuario -> convertToDto(usuario))
            .collect(Collectors.toList());
    }
}
