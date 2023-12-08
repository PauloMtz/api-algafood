package com.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.repository.RestauranteRepository;
import com.algafood.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private RestauranteService service;

    @GetMapping
    public List<Restaurante> listar() {
        return repository.listar();
    }

    /*@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {
        return service.salvar(restaurante);
    }

    {
        "nome": "Comida Teste",
        "taxaFrete": 12,
        "cozinha": {
            "id": 1
        }
    }*/

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
        try {
            restaurante = service.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable("restauranteId") Long id) {
        Restaurante restaurante = repository.buscar(id);

        if (restaurante != null) {
			return ResponseEntity.ok(restaurante);
		}

		//return ResponseEntity.notFound().build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, 
        @RequestBody Restaurante restaurante) {

        try {
			Restaurante persistido = repository.buscar(restauranteId);
			
			if (persistido != null) {
				BeanUtils.copyProperties(restaurante, persistido, "id");
				
				persistido = service.salvar(persistido);
				return ResponseEntity.ok(persistido);
			}
			
			return ResponseEntity.notFound().build();
		
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @DeleteMapping("/{restauranteId}")
	public ResponseEntity<?> remover(@PathVariable Long restauranteId) {
		try {
			service.excluir(restauranteId);	
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

    // ver aulas 4.33 e 4.34 sobre atualização parcial com o PATCH
    @PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos) {
		Restaurante restauranteAtual = repository.buscar(restauranteId);
		
		if (restauranteAtual == null) {
			return ResponseEntity.notFound().build();
		}
		
		gerenciar(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);
	}

    private void gerenciar(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
//			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
}
