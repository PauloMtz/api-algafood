package com.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.PedidoDtoAssembler;
import com.algafood.api.assembler.PedidoResumoDtoAssembler;
import com.algafood.api.model.dto.PedidoDto;
import com.algafood.api.model.dto.PedidoResumoDto;
import com.algafood.domain.model.Pedido;
import com.algafood.domain.repository.PedidoRepository;
import com.algafood.domain.service.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoRepository repository;
    
    @Autowired
    private PedidoService service;
    
    @Autowired
    private PedidoDtoAssembler assembler;

    @Autowired
    private PedidoResumoDtoAssembler pedidoResumoAssembler;
    
    @GetMapping
    public List<PedidoResumoDto> listar() {
        List<Pedido> listaPedidos = repository.findAll();
        
        return pedidoResumoAssembler.convertToCollectionDto(listaPedidos);
    }
    
    @GetMapping("/{pedidoId}")
    public PedidoDto buscar(@PathVariable Long pedidoId) {
        Pedido pedido = service.buscar(pedidoId);
        
        return assembler.convertToDto(pedido);
    }
}
