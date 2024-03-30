package com.algafood.api.model.dto;

import java.math.BigDecimal;

import com.algafood.api.model.jsonView.IRestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteDto {
    
	@JsonView({IRestauranteView.Resumo.class, IRestauranteView.ApenasNome.class})
    private Long id;

	@JsonView({IRestauranteView.Resumo.class, IRestauranteView.ApenasNome.class})
	private String nome;

	@JsonView(IRestauranteView.Resumo.class)
	private BigDecimal taxaFrete;

	@JsonView(IRestauranteView.Resumo.class)
	private CozinhaDto cozinha;
	
	private Boolean ativo;
	private Boolean aberto;
	private EnderecoDto endereco;
}
