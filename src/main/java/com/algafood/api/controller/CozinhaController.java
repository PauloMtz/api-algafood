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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.CozinhaDtoAssembler;
import com.algafood.api.assembler.CozinhaInputDtoDisassembler;
import com.algafood.api.model.dto.CozinhaDto;
import com.algafood.api.model.inputDto.CozinhaInputDto;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;
import com.algafood.domain.service.CozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
    
    @Autowired
	private CozinhaRepository repository;

	@Autowired
	private CozinhaService service;

	@Autowired
	private CozinhaDtoAssembler assembler;

	@Autowired
	private CozinhaInputDtoDisassembler disassembler;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaDto adicionar(@RequestBody 
		@Valid CozinhaInputDto cozinhaInputDto) {

		Cozinha cozinha = disassembler.convertToDomainObject(cozinhaInputDto);
		cozinha = service.salvar(cozinha);

		return assembler.convertToDto(cozinha);
	}

	@GetMapping("/{cozinhaId}")
	public CozinhaDto buscarPorId(@PathVariable("cozinhaId") Long id) {
		return assembler.convertToDto(service.buscar(id));
	}

	@GetMapping("/nome") // url: http://localhost:8080/cozinhas/nome?c=ind
	public List<CozinhaDto> cozinhasPorNome(@RequestParam("c") String nome) {
		return assembler
			.convertToCollectionDto(repository.findByNomeContaining(nome));
	}
	
	@GetMapping
	public List<CozinhaDto> listar() {
		return assembler.convertToCollectionDto(repository.findAll());
	}

	@PutMapping("/{cozinhaId}")
	public CozinhaDto atualizar(@PathVariable Long cozinhaId, 
		@RequestBody @Valid CozinhaInputDto cozinhaInputDto) {

		Cozinha persistida = service.buscar(cozinhaId);
			
		disassembler.copyToDomainObject(cozinhaInputDto, persistida);

		persistida = service.salvar(persistida);

		return assembler.convertToDto(persistida);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{cozinhaId}")
	public void remover(@PathVariable Long cozinhaId) {
		service.excluir(cozinhaId);
	}
}
