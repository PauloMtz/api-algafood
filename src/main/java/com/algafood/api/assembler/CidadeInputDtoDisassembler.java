package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.CidadeInputDto;
import com.algafood.domain.model.Cidade;
import com.algafood.domain.model.Estado;

@Component
public class CidadeInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Cidade convertToDomainObject(CidadeInputDto cidadeInputDto) {
        return modelMapper.map(cidadeInputDto, Cidade.class);
    }
    
    public void copyToDomainObject(CidadeInputDto cidadeInputDto, Cidade cidade) {

        /* para evitar org.springframework.orm.jpa.JpaSystemException: 
        identifier of an instance of com.algafood.domain.model.Estado 
        was altered from 1 to 2; nested exception is org.hibernate.
        HibernateException: identifier of an instance of 
        com.algafood.domain.model.Estado was altered from 1 to 2]*/
        cidade.setEstado(new Estado());
        
        modelMapper.map(cidadeInputDto, cidade);
    }
}
