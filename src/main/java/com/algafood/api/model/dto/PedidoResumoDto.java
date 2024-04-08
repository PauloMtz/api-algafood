package com.algafood.api.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

//import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

//@JsonFilter("filtroPedido")
@Getter
@Setter
public class PedidoResumoDto {
    
    private String codigo;
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;
    private String status;
    private OffsetDateTime dataCriacao;
    private RestauranteResumoDto restaurante;
    //private UsuarioDto cliente;
    private String nomeCliente;
}
