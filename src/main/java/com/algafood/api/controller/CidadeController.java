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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
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

    @GetMapping
    public List<Cidade> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
        try {
            cidade = service.salvar(cidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> buscarPorId(@PathVariable("cidadeId") Long id) {
        Optional<Cidade> cidade = repository.findById(id);

        if (cidade.isPresent()) {
			return ResponseEntity.ok(cidade.get());
		}

		return ResponseEntity.notFound().build();
    }

    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, 
        @RequestBody Cidade cidade) {

        try {
			Optional<Cidade> persistido = repository.findById(cidadeId);
			
			if (persistido != null) {
				BeanUtils.copyProperties(cidade, persistido.get(), "id");
				
				Cidade cidadeSalvar = service.salvar(persistido.get());
				return ResponseEntity.ok(cidadeSalvar);
			}
			
			return ResponseEntity.notFound().build();
		
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
        service.excluir(cidadeId);
	}
}
