package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.PedidoResumoDto;
import com.algafood.domain.model.Pedido;

@Component
public class PedidoResumoDtoAssembler {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public PedidoResumoDto convertToDto(Pedido pedido) {
        return modelMapper.map(pedido, PedidoResumoDto.class);
    }
    
    public List<PedidoResumoDto> convertToCollectionDto(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> convertToDto(pedido))
                .collect(Collectors.toList());
    }
}
