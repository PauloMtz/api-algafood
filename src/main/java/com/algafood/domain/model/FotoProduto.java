package com.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.algafood.core.validation.Groups;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoProduto {
    
	@NotNull(groups = Groups.EstadoId.class)
    @EqualsAndHashCode.Include
	@Id
	@Column(name = "produto_id")
	private Long id;

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;

	// ver artigo sobre mapeamento bidirecional one to one
	// nesse caso, não terá o mapeamento inverso na classe Produto
	// https://blog.algaworks.com/lazy-loading-com-mapeamento-onetoone/

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Produto produto;
}
