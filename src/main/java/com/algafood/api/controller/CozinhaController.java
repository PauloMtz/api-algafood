package com.algafood.api.controller;

import java.util.List;

//import org.springframework.beans.BeanUtils;
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

	/*
		---------------------------------------
					Métodos Http
		---------------------------------------
		. inserir: 		POST 	/cozinhas
		. buscar por Id: GET 	/cozinhas/{id}
		. listar todos: GET 	/cozinhas
		. editar: 		PUT 	/cozinhas/{id}
		. excluir: 		DELETE 	/cozinhas/{id}
		---------------------------------------

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void adicionar(@RequestBody Cozinha cozinha) {
		cozinhaRepository.salvar(cozinha);
	}

	@GetMapping("/{cozinhaId}")
	public Cozinha buscarPorId(@PathVariable("cozinhaId") Long id) {
		return cozinhaRepository.buscar(id);
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarPorId(@PathVariable("cozinhaId") Long id) {
		Cozinha cozinha = cozinhaRepository.buscar(id);
		//return ResponseEntity.status(HttpStatus.OK).body(cozinha);
		return ResponseEntity.ok(cozinha); // é a mesma coisa que a linha acima (shortcut)
	}
	
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {

		Cozinha persistida = cozinhaRepository.buscar(cozinhaId);

		try {
			if (persistida != null) {
				coservicezinhaRepository.remover(persistida);

				//return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				return ResponseEntity.noContent().build();
			}
			
			//return ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}*/

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return service.salvar(cozinha);
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarPorId(@PathVariable("cozinhaId") Long id) {
		Cozinha cozinha = cozinhaRepository.buscar(id);
		
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}

		//return ResponseEntity.notFound().build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.listar();
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,
		@RequestBody Cozinha cozinha) {

		Cozinha persistida = cozinhaRepository.buscar(cozinhaId);

		if (persistida != null) {
			persistida.setNome(cozinha.getNome());

			// essa linha abaixo faz a mesma coisa acima, ou seja,
			// copia os valores recebidos para o objeto persistido
			//BeanUtils.copyProperties(cozinha, persistida, "id");

			persistida = service.salvar(persistida);

			return ResponseEntity.ok(persistida);
		}

		//return ResponseEntity.notFound().build();
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
