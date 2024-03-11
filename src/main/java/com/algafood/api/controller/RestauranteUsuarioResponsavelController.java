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

import com.algafood.api.assembler.UsuarioDtoAssembler;
import com.algafood.api.model.dto.UsuarioDto;
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
    
    @Autowired
    private RestauranteService service;
    
    @Autowired
    private UsuarioDtoAssembler assembler;

    @GetMapping
    public List<UsuarioDto> listar(@PathVariable Long restauranteId) {

        Restaurante restaurante = service.buscar(restauranteId);
        
        return assembler.convertToCollectionDto(restaurante.getResponsaveis());
    }
    
    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long restauranteId, 
        @PathVariable Long usuarioId) {
        
        service.desassociarResponsavel(restauranteId, usuarioId);
    }
    
    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long restauranteId, 
        @PathVariable Long usuarioId) {
        
        service.associarResponsavel(restauranteId, usuarioId);
    }
}
