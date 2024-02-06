package com.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algafood.core.validation.Groups;
import com.algafood.core.validation.Multiplo;
import com.algafood.core.validation.ValorZeroIncluiDescricao;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

// se a propriedade taxaFrete = 0, a propriedade nome deve conter 'Frete Grátis'
@ValorZeroIncluiDescricao(valorField = "taxaFrete", 
	descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {
    
    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String nome;
	
	@NotNull
	@PositiveOrZero
	@Multiplo(numero = 5)
	@Column(nullable = false)
	private BigDecimal taxaFrete;

	// com a anotação @JsonIgnore não desserializa o atributo
	//@JsonIgnore
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime  dataCadastro;

	//@JsonIgnore
	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime  dataAtualizacao;
	
	// ignora o atributo nome da classe cozinha
	// caso seja informado ao serializar objeto restaurante
	// pode passar um array @JsonIgnoreProperties({"nome", "outroAtributo"})
	// pode permitir getters @JsonIgnoreProperties(value="nome", allowGetters=true)
	//@JsonIgnoreProperties("nome")
	@Valid
	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;

	//@JsonIgnore
	@Embedded
	private Endereco endereco;

	//@JsonIgnore
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento",
		joinColumns = @JoinColumn(name = "restaurante_id"),
		inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private List<FormaPagamento> formasPagamento = new ArrayList<>();

	//@JsonIgnore
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
}
