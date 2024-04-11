package com.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
//import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.PedidoDtoAssembler;
import com.algafood.api.assembler.PedidoInputDtoDisassembler;
import com.algafood.api.assembler.PedidoResumoDtoAssembler;
import com.algafood.api.model.dto.PedidoDto;
import com.algafood.api.model.dto.PedidoResumoDto;
import com.algafood.api.model.inputDto.PedidoInputDto;
import com.algafood.core.data.PageableTranslator;
import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.NegocioException;
import com.algafood.domain.filter.PedidoFilter;
import com.algafood.domain.model.Pedido;
import com.algafood.domain.model.Usuario;
import com.algafood.domain.repository.PedidoRepository;
import com.algafood.domain.repository.specification.PedidoSpecification;
import com.algafood.domain.service.PedidoService;
//import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
//import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.collect.ImmutableMap;

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

    /*
    // Aula 13.02 - selecionando campos retornados pela API com JsonFilter do Jackson
    // foi adicionado @JsonFilter("filtroPedido") em PedidoResumoDto.java
    @GetMapping
    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
        
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoResumoDto> pedidosDto = pedidoResumoAssembler
            .convertToCollectionDto(pedidos);
        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosDto);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("filtroPedido", SimpleBeanPropertyFilter.serializeAll());
        //filterProvider.addFilter("filtroPedido", SimpleBeanPropertyFilter.filterOutAllExcept("codigo", "valorTotal"));
        
        if (StringUtils.isNotBlank(campos)) {
            // http://localhost:8080/pedidos?campos=codigo,valorTotal
            filterProvider.addFilter("filtroPedido", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
        }
        
        pedidosWrapper.setFilters(filterProvider);

        return pedidosWrapper;
    }
    */
    
    /*@GetMapping
    public List<PedidoResumoDto> listar() {
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        
        return pedidoResumoAssembler.convertToCollectionDto(listaPedidos);
    }*/

    // http://localhost:8080/pedidos?clienteId=1&restauranteId=1
    // http://localhost:8080/pedidos?restauranteId=1&dataInicio=2024-04-03T19:56:00Z
    // http://localhost:8080/pedidos?restauranteId=1&dataInicio=2019-10-30T12:00:00Z&dataFim=2019-10-31T12:00:00Z
    /*@GetMapping
    public List<PedidoResumoDto> pesquisar(PedidoFilter filtro) {
        List<Pedido> listaPedidos = pedidoRepository.findAll(PedidoSpecification.usandoFiltro(filtro));
        
        return pedidoResumoAssembler.convertToCollectionDto(listaPedidos);
    }*/

    // http://localhost:8080/pedidos?clienteId=1&restauranteId=1&page=0
    @GetMapping
    public Page<PedidoResumoDto> pesquisar(PedidoFilter filtro,
        @PageableDefault(size = 2) Pageable pageable) {
        
        pageable = converterPageable(pageable);
        
        Page<Pedido> listaPedidos = pedidoRepository.findAll(PedidoSpecification.usandoFiltro(filtro), pageable);
        
        List<PedidoResumoDto> pedidosResumoDto = pedidoResumoAssembler.convertToCollectionDto(listaPedidos.getContent());
        
        Page<PedidoResumoDto> pedidosResumoDtoPage = new PageImpl<>(
            pedidosResumoDto, pageable, listaPedidos.getTotalElements());

        return pedidosResumoDtoPage;
    }
    
    // http://localhost:8080/pedidos?page=0&sort=nomeCliente,desc
    private Pageable converterPageable(Pageable apiPageable) {
        
        var mapeamento = ImmutableMap.of(
            "codigo", "codigo",
            "restaurante.nome", "restaurante.nome",
            "nomeCliente", "cliente.nome",
            "valorTotal", "valorTotal"
        );

        // com.algafood.core.data.PageableTranslator
        return PageableTranslator.translate(apiPageable, mapeamento);
    }

    @GetMapping("/{codigoPedido}")
    public PedidoDto buscar(@PathVariable String codigoPedido) {
        Pedido pedido = pedidoService.buscar(codigoPedido);
        
        return assembler.convertToDto(pedido);
    }
}
