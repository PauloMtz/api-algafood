package com.algafood.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoFotoDto {
    
    private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
}
