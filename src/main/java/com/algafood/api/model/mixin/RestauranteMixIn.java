package com.algafood.api.model.mixin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algafood.domain.model.Cozinha;
import com.algafood.domain.model.Endereco;
import com.algafood.domain.model.FormaPagamento;
import com.algafood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// classe que desserializa as propriedades @JsonIgnore
public abstract class RestauranteMixIn {
    
    @JsonIgnoreProperties(value = "nome", allowGetters = true)
	private Cozinha cozinha;
	
	@JsonIgnore
	private Endereco endereco;
	
	// se quiser mostrar esta data na desserialização
	// basta comentar esta anotação
	@JsonIgnore
	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	private LocalDateTime dataAtualizacao;
	
	@JsonIgnore
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>();
}
