package com.algafood.api.model.inputDto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoInputDto {
    
    @Valid
    @NotNull
    private RestauranteIdInputDto restaurante;
    
    @Valid
    @NotNull
    private EnderecoInputDto enderecoEntrega;
    
    @Valid
    @NotNull
    private FormaPagamentoIdInputDto formaPagamento;
    
    @Valid
    @Size(min = 1)
    @NotNull
    private List<ItemPedidoInputDto> itens;
}
