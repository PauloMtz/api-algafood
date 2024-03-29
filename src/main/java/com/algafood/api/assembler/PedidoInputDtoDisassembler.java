package com.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.PedidoInputDto;
import com.algafood.domain.model.Pedido;

@Component
public class PedidoInputDtoDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public Pedido toDomainObject(PedidoInputDto pedidoInputDto) {
        return modelMapper.map(pedidoInputDto, Pedido.class);
    }
    
    public void copyToDomainObject(PedidoInputDto pedidoInputDto, Pedido pedido) {
        modelMapper.map(pedidoInputDto, pedido);
    }
}
