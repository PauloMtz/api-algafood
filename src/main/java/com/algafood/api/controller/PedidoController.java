package com.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.PedidoDtoAssembler;
import com.algafood.api.assembler.PedidoInputDtoDisassembler;
import com.algafood.api.assembler.PedidoResumoDtoAssembler;
import com.algafood.api.model.dto.PedidoDto;
import com.algafood.api.model.dto.PedidoResumoDto;
import com.algafood.api.model.inputDto.PedidoInputDto;
import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.NegocioException;
import com.algafood.domain.model.Pedido;
import com.algafood.domain.model.Usuario;
import com.algafood.domain.repository.PedidoRepository;
import com.algafood.domain.service.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoDtoAssembler assembler;

    @Autowired
    private PedidoInputDtoDisassembler disassembler;

    @Autowired
    private PedidoResumoDtoAssembler pedidoResumoAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDto adicionar(@Valid @RequestBody PedidoInputDto pedidoInputDto) {
        try {
            Pedido novoPedido = disassembler.toDomainObject(pedidoInputDto);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = pedidoService.emitir(novoPedido);

            return assembler.convertToDto(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    @GetMapping
    public List<PedidoResumoDto> listar() {
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        
        return pedidoResumoAssembler.convertToCollectionDto(listaPedidos);
    }
    
    @GetMapping("/{codigoPedido}")
    public PedidoDto buscar(@PathVariable String codigoPedido) {
        Pedido pedido = pedidoService.buscar(codigoPedido);
        
        return assembler.convertToDto(pedido);
    }
}
