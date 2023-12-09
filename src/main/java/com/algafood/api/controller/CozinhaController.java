package com.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;
import com.algafood.domain.service.CozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
    
    @Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CozinhaService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return service.salvar(cozinha);
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarPorId(@PathVariable("cozinhaId") Long id) {
		Optional<Cozinha> cozinha = cozinhaRepository.findById(id);
		
		if (cozinha.isPresent()) {
			return ResponseEntity.ok(cozinha.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/nome")
	public List<Cozinha> cozinhasPorNome(@RequestParam("c") String nome) {
		return cozinhaRepository.listaPorNome(nome); // url: http://localhost:8080/cozinhas/nome?c=ind
	}
	
	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,
		@RequestBody Cozinha cozinha) {

		Optional<Cozinha> persistida = cozinhaRepository.findById(cozinhaId);

		if (persistida.isPresent()) {
			BeanUtils.copyProperties(cozinha, persistida.get(), "id");

			Cozinha cozinhaSalvar = service.salvar(persistida.get());

			return ResponseEntity.ok(cozinhaSalvar);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {

		try {
			service.excluir(cozinhaId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException ex) {
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoRemoverException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
	}
}
