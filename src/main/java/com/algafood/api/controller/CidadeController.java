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

import com.algafood.api.assembler.CidadeDtoAssembler;
import com.algafood.api.assembler.CidadeInputDtoDisassembler;
import com.algafood.api.model.dto.CidadeDto;
import com.algafood.api.model.inputDto.CidadeInputDto;
import com.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algafood.domain.exception.NegocioException;
import com.algafood.domain.model.Cidade;
import com.algafood.domain.repository.CidadeRepository;
import com.algafood.domain.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
    
    @Autowired
    private CidadeRepository repository;

    @Autowired
    private CidadeService service;

    @Autowired
    private CidadeDtoAssembler assembler;

    @Autowired
    private CidadeInputDtoDisassembler disassembler;

    @GetMapping
    public List<CidadeDto> listar() {
        return assembler.convertToCollectionDto(repository.findAll());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CidadeDto adicionar(@RequestBody @Valid CidadeInputDto cidadeInputDto) {
        
        try {
            Cidade cidade = disassembler.convertToDomainObject(cidadeInputDto);
            cidade = service.salvar(cidade);
            return assembler.convertToDto(cidade);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @GetMapping("/{cidadeId}")
    public CidadeDto buscarPorId(@PathVariable("cidadeId") Long id) {
		return assembler.convertToDto(service.buscar(id));
    }

    @PutMapping("/{cidadeId}")
    public CidadeDto atualizar(@PathVariable Long cidadeId, 
        @RequestBody @Valid CidadeInputDto cidadeInputDto) {

        try {
            Cidade persistida = service.buscar(cidadeId);

            disassembler.copyToDomainObject(cidadeInputDto, persistida);
	
			persistida = service.salvar(persistida);

            return assembler.convertToDto(persistida);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
        service.excluir(cidadeId);
	}
}
