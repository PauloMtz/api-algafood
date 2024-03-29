package com.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.FormaPagamentoDtoAssembler;
import com.algafood.api.model.dto.FormaPagamentoDto;
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {

    @Autowired
    private RestauranteService service;

    @Autowired
    private FormaPagamentoDtoAssembler assembler;

    @GetMapping
    public List<FormaPagamentoDto> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = service.buscar(restauranteId);
        return assembler.convertToCollectionDto(restaurante.getFormasPagamento());
    }

    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerFormaPagamento(@PathVariable Long restauranteId, 
        @PathVariable Long formaPagamentoId) {

        service.removerFormaPagamento(restauranteId, formaPagamentoId);
    }

    @PutMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarFormaPagamento(@PathVariable Long restauranteId, 
        @PathVariable Long formaPagamentoId) {

        service.adicionarFormaPagamento(restauranteId, formaPagamentoId);
    }
}
