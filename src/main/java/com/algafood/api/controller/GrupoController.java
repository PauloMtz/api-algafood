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

import com.algafood.api.assembler.GrupoDtoAssembler;
import com.algafood.api.assembler.GrupoInputDtoDisassembler;
import com.algafood.api.model.dto.GrupoDto;
import com.algafood.api.model.inputDto.GrupoInputDto;
import com.algafood.domain.model.Grupo;
import com.algafood.domain.repository.GrupoRepository;
import com.algafood.domain.service.GrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    
    @Autowired
    private GrupoRepository repository;
    
    @Autowired
    private GrupoService service;
    
    @Autowired
    private GrupoDtoAssembler assembler;
    
    @Autowired
    private GrupoInputDtoDisassembler disassembler;

    @GetMapping
	public List<GrupoDto> listar() {

		List<Grupo> listaTodos = repository.findAll();

		return assembler.convertToCollectionDto(listaTodos);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
    public GrupoDto adicionar(@RequestBody @Valid GrupoInputDto grupoInputDto) {

		Grupo grupo = disassembler.convertToDomainObject(grupoInputDto);
		grupo = service.salvar(grupo);

		return assembler.convertToDto(grupo);
    }

	@GetMapping("/{grupoId}")
	public GrupoDto buscarPorId(@PathVariable("grupoId") Long id) {

		Grupo grupo = service.buscar(id);

		return assembler.convertToDto(grupo);
	}

	@PutMapping("/{grupoId}")
	public GrupoDto atualizar(@PathVariable Long grupoId, 
		@RequestBody @Valid GrupoInputDto grupoInputDto) {

		Grupo persistido = service.buscar(grupoId);
			
		disassembler.copyToDomainObject(grupoInputDto, persistido);

		persistido = service.salvar(persistido);

		return assembler.convertToDto(persistido);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{grupoId}")
	public void remover(@PathVariable Long grupoId) {
		service.excluir(grupoId);
	}
}
