package com.algafood.api.model.inputDto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoInputDto {
    
    @NotBlank
    private String nome;
    
    @NotBlank
    private String descricao;
    
    @NotNull
    @PositiveOrZero
    private BigDecimal preco;
    
    @NotNull
    private Boolean ativo;
}
