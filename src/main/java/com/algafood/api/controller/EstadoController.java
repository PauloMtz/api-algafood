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
import org.springframework.web.bind.annotation.RestController;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.model.Estado;
import com.algafood.domain.repository.EstadoRepository;
import com.algafood.domain.service.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoController {
    
    @Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private EstadoService estadoService;
	
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}

	@PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Estado estado) {
        try {
            estado = estadoService.salvar(estado);
            return ResponseEntity.status(HttpStatus.CREATED).body(estado);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

	@GetMapping("/{estadoId}")
	public ResponseEntity<Estado> buscarPorId(@PathVariable("estadoId") Long id) {
		Optional<Estado> estado = estadoRepository.findById(id);

		if (estado.isPresent()) {
			return ResponseEntity.ok(estado.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{estadoId}")
	public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId,
		@RequestBody Estado estado) {

		Optional<Estado> persistido = estadoRepository.findById(estadoId);

		if (persistido.isPresent()) {
			BeanUtils.copyProperties(estado, persistido.get(), "id");

			Estado estadoSalvar = estadoService.salvar(persistido.get());

			return ResponseEntity.ok(estadoSalvar);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{estadoId}")
	public ResponseEntity<?> remover(@PathVariable Long estadoId) {

		try {
			estadoService.excluir(estadoId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException ex) {
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoRemoverException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
	}
}
