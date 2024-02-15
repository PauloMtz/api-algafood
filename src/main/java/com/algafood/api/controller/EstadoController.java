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

import com.algafood.api.assembler.EstadoDtoAssembler;
import com.algafood.api.assembler.EstadoInputDtoDisassembler;
import com.algafood.api.model.dto.EstadoDto;
import com.algafood.api.model.inputDto.EstadoInputDto;
import com.algafood.domain.model.Estado;
import com.algafood.domain.repository.EstadoRepository;
import com.algafood.domain.service.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoController {
    
    @Autowired
	private EstadoRepository repository;

	@Autowired
	private EstadoService service;

	@Autowired
	private EstadoDtoAssembler assembler;

	@Autowired
	private EstadoInputDtoDisassembler disassembler;
	
	@GetMapping
	public List<EstadoDto> listar() {

		List<Estado> listaTodos = repository.findAll();

		return assembler.convertToCollectionDto(listaTodos);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
    public EstadoDto adicionar(@RequestBody @Valid EstadoInputDto estadoInputDto) {

		Estado estado = disassembler.convertToDomainObject(estadoInputDto);
		estado = service.salvar(estado);

		return assembler.convertToDto(estado);
    }

	@GetMapping("/{estadoId}")
	public EstadoDto buscarPorId(@PathVariable("estadoId") Long id) {

		Estado estado = service.buscar(id);

		return assembler.convertToDto(estado);
	}

	@PutMapping("/{estadoId}")
	public EstadoDto atualizar(@PathVariable Long estadoId, 
		@RequestBody @Valid EstadoInputDto estadoInputDto) {

		Estado persistido = service.buscar(estadoId);
			
		disassembler.copyToDomainObject(estadoInputDto, persistido);

		persistido = service.salvar(persistido);

		return assembler.convertToDto(persistido);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{estadoId}")
	public void remover(@PathVariable Long estadoId) {
		service.excluir(estadoId);
	}
}
