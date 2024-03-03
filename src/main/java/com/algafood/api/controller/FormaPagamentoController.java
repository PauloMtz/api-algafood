package com.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.FormaPagamentoDtoAssembler;
import com.algafood.api.assembler.FormaPagamentoInputDtoDisassembler;
import com.algafood.api.model.dto.FormaPagamentoDto;
import com.algafood.api.model.inputDto.FormaPagamentoInputDto;
import com.algafood.domain.model.FormaPagamento;
import com.algafood.domain.repository.FormaPagamentoRepository;
import com.algafood.domain.service.FormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
    
    @Autowired
    private FormaPagamentoRepository repository;
    
    @Autowired
    private FormaPagamentoService service;
    
    @Autowired
    private FormaPagamentoDtoAssembler assembler;
    
    @Autowired
    private FormaPagamentoInputDtoDisassembler disassembler;

    @GetMapping
    public List<FormaPagamentoDto> listar() {
        
        return assembler.convertToCollectionDto(repository.findAll());
    }

    @GetMapping("/{formaPagamentoId}")
    public FormaPagamentoDto buscar(@PathVariable Long formaPagamentoId) {
        
        return assembler.convertToDto(service.buscar(formaPagamentoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoDto adicionar(@RequestBody 
        @Valid FormaPagamentoInputDto formaPagamentoInputDto) {
        
        FormaPagamento formaPagamento = disassembler.convertToDomainObject(formaPagamentoInputDto);
        
        formaPagamento = service.salvar(formaPagamento);
        
        return assembler.convertToDto(formaPagamento);
    }

    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoDto atualizar(@PathVariable Long formaPagamentoId,
        @RequestBody @Valid FormaPagamentoInputDto formaPagamentoInputDto) {
            
        FormaPagamento formaPagamento = service.buscar(formaPagamentoId);
        
        disassembler.copyToDomainObject(formaPagamentoInputDto, formaPagamento);
        
        formaPagamento = service.salvar(formaPagamento);
        
        return assembler.convertToDto(formaPagamento);
    }

    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {
        
        service.excluir(formaPagamentoId);	
    } 
}
