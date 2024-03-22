package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.PedidoDto;
import com.algafood.domain.model.Pedido;

@Component
public class PedidoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public PedidoDto convertToDto(Pedido pedido) {
        return modelMapper.map(pedido, PedidoDto.class);
    }
    
    public List<PedidoDto> convertToCollectionDto(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> convertToDto(pedido))
                .collect(Collectors.toList());
    }
}
