package com.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;

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

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.NegocioException;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {
        // se não encontrar entidade associada (Cozinha), lança BAD_REQUEST
        try {
            return service.salvar(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @GetMapping("/{restauranteId}")
    public Restaurante buscarPorId(@PathVariable("restauranteId") Long id) {
		return service.buscar(id);
    }

    @GetMapping("/taxa-frete")
    public List<Restaurante> porTaxaFrete(BigDecimal taxaIni, BigDecimal taxaFim) {
        // url: http://localhost:8080/restaurantes/taxa-frete?taxaIni=10&taxaFim=13
        return repository.findByTaxaFreteBetween(taxaIni, taxaFim);
    }

    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {

        Restaurante persistido = service.buscar(restauranteId);
				
        BeanUtils.copyProperties(restaurante, persistido,
            "id", "formasPagamento", "endereco", "dataCadastro");

        // se não encontrar entidade principal (Restaurante), lança NOT_FOUND
        // se não encontrar entidade associada (Cozinha), lança BAD_REQUEST
        try {
            return service.salvar(persistido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{restauranteId}")
	public void remover(@PathVariable Long restauranteId) {
        service.excluir(restauranteId);
	}
}
