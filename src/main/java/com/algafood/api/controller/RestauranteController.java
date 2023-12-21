package com.algafood.api.controller;

import java.math.BigDecimal;
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
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.repository.RestauranteRepository;
import com.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private RestauranteService service;

    @GetMapping
    public List<Restaurante> listar() {
        return repository.findAll();
    }

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
        Optional<Restaurante> restaurante = repository.findById(id);

        if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/taxa-frete")
    public List<Restaurante> porTaxaFrete(BigDecimal taxaIni, BigDecimal taxaFim) {
        // url: http://localhost:8080/restaurantes/taxa-frete?taxaIni=10&taxaFim=13
        return repository.findByTaxaFreteBetween(taxaIni, taxaFim);
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, 
        @RequestBody Restaurante restaurante) {

        try {
			Optional<Restaurante> persistido = repository.findById(restauranteId);
			
			if (persistido != null) {
				BeanUtils.copyProperties(restaurante, persistido.get(),
                     "id", "formasPagamento", "endereco", "dataCadastro");
				
				Restaurante restauranteSalvar = service.salvar(persistido.get());
				return ResponseEntity.ok(restauranteSalvar);
			}
			
			return ResponseEntity.notFound().build();
		
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{restauranteId}")
	public void remover(@PathVariable Long restauranteId) {
        service.excluir(restauranteId);
	}
}
