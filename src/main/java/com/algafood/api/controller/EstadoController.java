package com.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
	
	@GetMapping
	public List<Estado> listar() {
		return repository.findAll();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
    public Estado adicionar(@RequestBody @Valid Estado estado) {
		return service.salvar(estado);
    }

	@GetMapping("/{estadoId}")
	public Estado buscarPorId(@PathVariable("estadoId") Long id) {
		return service.buscar(id);
	}

	@PutMapping("/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId, 
		@RequestBody @Valid Estado estado) {

		Estado persistido = service.buscar(estadoId);
			
		BeanUtils.copyProperties(estado, persistido, "id");

		return service.salvar(persistido);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{estadoId}")
	public void remover(@PathVariable Long estadoId) {
		service.excluir(estadoId);
	}
}
