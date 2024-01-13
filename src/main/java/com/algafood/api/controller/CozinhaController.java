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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
		return service.salvar(cozinha);
	}

	@GetMapping("/{cozinhaId}")
	public Cozinha buscarPorId(@PathVariable("cozinhaId") Long id) {
		return service.buscar(id);
	}

	@GetMapping("/nome")
	public List<Cozinha> cozinhasPorNome(@RequestParam("c") String nome) {
		return repository.findByNomeContaining(nome); // url: http://localhost:8080/cozinhas/nome?c=ind
	}
	
	@GetMapping
	public List<Cozinha> listar() {
		return repository.findAll();
	}

	@PutMapping("/{cozinhaId}")
	public Cozinha atualizar(@PathVariable Long cozinhaId, 
		@RequestBody @Valid Cozinha cozinha) {

		Cozinha persistida = service.buscar(cozinhaId);
			
		BeanUtils.copyProperties(cozinha, persistida, "id");

		return service.salvar(persistida);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{cozinhaId}")
	public void remover(@PathVariable Long cozinhaId) {
		service.excluir(cozinhaId);
	}
}
