package com.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.ProdutoDtoAssembler;
import com.algafood.api.assembler.ProdutoInputDtoDisassembler;
import com.algafood.api.model.dto.ProdutoDto;
import com.algafood.api.model.inputDto.ProdutoInputDto;
import com.algafood.domain.model.Produto;
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.repository.ProdutoRepository;
import com.algafood.domain.service.ProdutoService;
import com.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoRepository repository;
    
    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private RestauranteService restauranteService;
    
    @Autowired
    private ProdutoDtoAssembler assembler;
    
    @Autowired
    private ProdutoInputDtoDisassembler disassembler;
    
    @GetMapping
    public List<ProdutoDto> listar(@PathVariable Long restauranteId) {

        Restaurante restaurante = restauranteService.buscar(restauranteId);
        
        List<Produto> listaTodos = repository.findByRestaurante(restaurante);
        
        return assembler.convertToCollectionDto(listaTodos);
    }
    
    @GetMapping("/{produtoId}")
    public ProdutoDto buscar(@PathVariable Long restauranteId, 
        @PathVariable Long produtoId) {
        
        Produto produto = produtoService.buscar(restauranteId, produtoId);
        
        return assembler.convertToDto(produto);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDto adicionar(@PathVariable Long restauranteId,
        @RequestBody @Valid ProdutoInputDto produtoInputDto) {
        
        Restaurante restaurante = restauranteService.buscar(restauranteId);
        
        Produto produto = disassembler.convertToDomainObject(produtoInputDto);
        
        produto.setRestaurante(restaurante);
        produto = produtoService.salvar(produto);
        
        return assembler.convertToDto(produto);
    }
    
    @PutMapping("/{produtoId}")
    public ProdutoDto atualizar(@PathVariable Long restauranteId, 
        @PathVariable Long produtoId, @RequestBody 
        @Valid ProdutoInputDto produtoInputDto) {

        Produto persistido = produtoService.buscar(restauranteId, produtoId);
        
        disassembler.copyToDomainObject(produtoInputDto, persistido);
        
        persistido = produtoService.salvar(persistido);
        
        return assembler.convertToDto(persistido);
    }
}
