package com.algafood.api.model.dto;

import com.algafood.api.model.jsonView.IRestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CozinhaDto {
    
    @JsonView(IRestauranteView.Resumo.class)
    private Long id;

    @JsonView(IRestauranteView.Resumo.class)
	private String nome;
}
