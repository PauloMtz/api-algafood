package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.UsuarioInputDto;
import com.algafood.domain.model.Usuario;

@Component
public class UsuarioInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Usuario convertToDomainObject(UsuarioInputDto usuarioInputDto) {
        return modelMapper.map(usuarioInputDto, Usuario.class);
    }
    
    public void copyToDomainObject(UsuarioInputDto usuarioInputDto, Usuario usuario) {
        modelMapper.map(usuarioInputDto, usuario);
    }
}
